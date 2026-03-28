package com.paymentreminder.data.export

import android.util.Base64
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.SecureRandom
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object ExportImportManager {

    private const val PASSPHRASE = "PReminder#Xport@2024!Sc"
    private const val SALT = "PRBkSalt8x"
    private const val VERSION = 2

    private val secretKey by lazy {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(PASSPHRASE.toCharArray(), SALT.toByteArray(), 65536, 256)
        SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
    }

    fun encode(payments: List<ExportPayment>): ByteArray {
        val json = toJson(payments)
        val compressed = gzipCompress(json.toByteArray(Charsets.UTF_8))
        val iv = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(compressed)
        val combined = iv + encrypted
        return Base64.encode(combined, Base64.NO_WRAP)
    }

    fun decode(data: ByteArray): List<ExportPayment> {
        val combined = Base64.decode(data, Base64.NO_WRAP)
        val iv = combined.copyOfRange(0, 16)
        val encrypted = combined.copyOfRange(16, combined.size)
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val compressed = cipher.doFinal(encrypted)
        val json = gzipDecompress(compressed).toString(Charsets.UTF_8)
        return fromJson(json)
    }

    private fun toJson(payments: List<ExportPayment>): String {
        val root = JSONObject()
        root.put("v", VERSION)
        root.put("ts", System.currentTimeMillis())
        val arr = JSONArray()
        for (p in payments) {
            val obj = JSONObject()
            obj.put("vn", p.vendorName)
            obj.put("in", p.invoiceNumber)
            obj.put("dd", p.dueDateEpoch)
            obj.put("am", p.amount)
            obj.put("ip", p.isPaid)
            if (p.paidDateEpoch != null) obj.put("pd", p.paidDateEpoch) else obj.put("pd", JSONObject.NULL)
            obj.put("mr", p.isMonthlyReminder)
            obj.put("ca", p.createdAt)
            val cn = JSONArray()
            p.customNotifications.forEach { cn.put(it) }
            obj.put("cn", cn)
            arr.put(obj)
        }
        root.put("data", arr)
        return root.toString()
    }

    private fun fromJson(json: String): List<ExportPayment> {
        val root = JSONObject(json)
        val version = root.optInt("v", 1)
        val arr = root.getJSONArray("data")
        val result = mutableListOf<ExportPayment>()
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)

            // v1 used "ns" for all notifications, v2 uses "cn" for custom only
            val notifKey = if (version >= 2) "cn" else "ns"
            val ns = obj.optJSONArray(notifKey) ?: JSONArray()
            val notifications = mutableListOf<Long>()
            for (j in 0 until ns.length()) {
                notifications.add(ns.getLong(j))
            }

            result.add(
                ExportPayment(
                    vendorName = obj.getString("vn"),
                    invoiceNumber = obj.optString("in", ""),
                    dueDateEpoch = obj.getLong("dd"),
                    amount = obj.getDouble("am"),
                    isPaid = obj.getBoolean("ip"),
                    paidDateEpoch = if (obj.isNull("pd")) null else obj.getLong("pd"),
                    isMonthlyReminder = obj.getBoolean("mr"),
                    customNotifications = notifications,
                    createdAt = obj.optLong("ca", System.currentTimeMillis())
                )
            )
        }
        return result
    }

    private fun gzipCompress(data: ByteArray): ByteArray {
        val bos = ByteArrayOutputStream()
        GZIPOutputStream(bos).use { it.write(data) }
        return bos.toByteArray()
    }

    private fun gzipDecompress(data: ByteArray): ByteArray {
        return GZIPInputStream(ByteArrayInputStream(data)).use { it.readBytes() }
    }
}
