package com.routeboost.app

import android.net.VpnService

/**
 * Safe starter service only.
 *
 * This service intentionally does not establish a TUN interface or forward packets.
 * Implementing a real VPN requires a reviewed tunnel protocol, authenticated server,
 * DNS handling, IPv4/IPv6 routing, MTU handling, reconnect logic, and leak protection.
 */
class RouteBoostVpnService : VpnService()
