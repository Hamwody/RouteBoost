package com.routeboost.app

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.VpnService
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private val vpnRequestCode = 1001
    private lateinit var statusText: TextView
    private lateinit var permissionButton: Button

    private val bg = Color.rgb(9, 6, 21)
    private val panel = Color.rgb(25, 18, 43)
    private val purple = Color.rgb(168, 85, 247)
    private val white = Color.rgb(248, 246, 255)
    private val muted = Color.rgb(177, 166, 205)
    private val green = Color.rgb(74, 222, 128)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        buildScreen()
    }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(18), dp(16), dp(18), dp(16))
        background = rounded(panel, dp(20), Color.rgb(57, 40, 88), dp(1))
    }

    private fun label(value: String, size: Float, color: Int, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
            includeFontPadding = true
        }

    private fun buildScreen() {
        val scroll = ScrollView(this).apply { setBackgroundColor(bg); clipToPadding = false }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(22), dp(22), dp(28))
        }

        root.addView(label("R  ROUTEBOOST", 25f, white, true), margin(bottom = 2))
        root.addView(label("PLAY SMOOTHER. LAG LESS.", 11f, purple, true), margin(bottom = 22))
        root.addView(label("Minecraft network optimizer", 22f, white, true), margin(bottom = 6))
        root.addView(label("A connection dashboard for Minecraft Bedrock.", 14f, muted), margin(bottom = 20))

        val statusCard = card()
        statusCard.addView(label("CONNECTION STATUS", 11f, muted, true), margin(bottom = 8))
        statusText = label("Not connected", 23f, white, true)
        statusCard.addView(statusText)
        statusCard.addView(label("No VPN tunnel is running.", 13f, muted), margin(top = 4))
        root.addView(statusCard, margin(bottom = 14))

        val profile = card()
        profile.addView(label("GAME PROFILE", 11f, muted, true), margin(bottom = 8))
        profile.addView(label("Minecraft Bedrock", 19f, white, true), margin(bottom = 3))
        profile.addView(label("Traffic scope: whole device", 13f, muted))
        root.addView(profile, margin(bottom = 14))

        val stats = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        stats.addView(statCard("PING", "—", "Not measured"), LinearLayout.LayoutParams(0, dp(108), 1f).apply { marginEnd = dp(6) })
        stats.addView(statCard("PACKET LOSS", "—", "Not measured"), LinearLayout.LayoutParams(0, dp(108), 1f).apply { marginStart = dp(6) })
        root.addView(stats, margin(bottom = 14))

        permissionButton = Button(this).apply {
            text = "Grant VPN permission"
            isAllCaps = false
            textSize = 16f
            setTextColor(Color.WHITE)
            background = rounded(purple, dp(16), purple, 0)
            setPadding(dp(14), dp(8), dp(14), dp(8))
            setOnClickListener { requestVpnPermission() }
        }
        root.addView(permissionButton, LinearLayout.LayoutParams(-1, dp(54)).apply { bottomMargin = dp(12) })

        val note = card()
        note.addView(label("IMPORTANT", 11f, purple, true), margin(bottom = 6))
        note.addView(label(
            "This is an early native APK prototype. VPN permission is available, but packet forwarding, route selection, live game measurements, and optimization servers are not implemented yet. It will not reduce Minecraft ping or packet loss at this stage.",
            13f, muted
        ))
        root.addView(note, margin(bottom = 20))
        root.addView(label("BUILD 0.2.0  •  ANDROID", 10f, muted, true))

        scroll.addView(root)
        setContentView(scroll)
        refreshPermissionState()
    }

    private fun statCard(title: String, value: String, caption: String): LinearLayout {
        val box = card()
        box.addView(label(title, 10f, muted, true), margin(bottom = 9))
        box.addView(label(value, 24f, white, true), margin(bottom = 2))
        box.addView(label(caption, 10f, muted))
        return box
    }

    @Suppress("DEPRECATION")
    private fun requestVpnPermission() {
        val intent = VpnService.prepare(this)
        if (intent == null) {
            statusText.text = "VPN permission granted"
            permissionButton.text = "VPN permission granted"
        } else {
            startActivityForResult(intent, vpnRequestCode)
        }
    }

    private fun refreshPermissionState() {
        val intent = VpnService.prepare(this)
        if (intent == null) {
            statusText.text = "VPN permission granted"
            permissionButton.text = "VPN permission granted"
        }
    }

    @Deprecated("Kept for broad Android compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == vpnRequestCode) {
            if (resultCode == RESULT_OK) {
                statusText.text = "VPN permission granted"
                permissionButton.text = "VPN permission granted"
            } else {
                statusText.text = "Permission not granted"
            }
        }
    }

    private fun rounded(color: Int, radius: Int, strokeColor: Int, strokeWidth: Int): GradientDrawable =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius.toFloat()
            if (strokeWidth > 0) setStroke(strokeWidth, strokeColor)
        }

    private fun margin(bottom: Int = 0, top: Int = 0): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(-1, -2).apply {
            this.bottomMargin = bottom
            this.topMargin = top
        }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
