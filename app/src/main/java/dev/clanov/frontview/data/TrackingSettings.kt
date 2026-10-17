package dev.clanov.frontview.data

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class TrackingSettings(context: Context) {
    private val store = context.getSharedPreferences("parcel", Context.MODE_PRIVATE)
    private val alias = "frontview_tracking_api"

    fun isConfigured(): Boolean = key().isNotBlank() && carrier().isNotBlank() && invoice().isNotBlank()
    fun carrier(): String = store.getString("carrier", "").orEmpty()
    fun invoice(): String = store.getString("invoice", "").orEmpty()

    private fun keyMaterial(): SecretKey {
        val store = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        val existing = store.getKey(alias, null)
        if (existing is SecretKey) return existing
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore").apply {
            init(KeyGenParameterSpec.Builder(alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).build())
        }.generateKey()
    }

    fun key(): String = runCatching {
        val bytes = Base64.decode(store.getString("key", "") ?: "", Base64.NO_WRAP)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, keyMaterial(), GCMParameterSpec(128, bytes.copyOfRange(0, 12)))
        String(cipher.doFinal(bytes.copyOfRange(12, bytes.size)), Charsets.UTF_8)
    }.getOrDefault("")

    fun save(key: String, carrier: String, invoice: String) {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, keyMaterial())
        val bytes = cipher.iv + cipher.doFinal(key.toByteArray(Charsets.UTF_8))
        store.edit()
            .putString("key", Base64.encodeToString(bytes, Base64.NO_WRAP))
            .putString("carrier", carrier).putString("invoice", invoice).apply()
    }
}
