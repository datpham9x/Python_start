firewall {
    all-ping enable
    broadcast-ping disable
    group {
	address-group fwketoan {
            address 192.168.10.228
        }
        network-group PRIVATE_NETS {
            network 192.168.0.0/16
            network 172.16.0.0/12
            network 10.0.0.0/8
        }
    }
    ipv6-receive-redirects disable
    ipv6-src-route disable
    ip-src-route disable
    log-martians disable
    modify balance {
        rule 10 {
            action modify
            description "do NOT load balance lan to lan"
            destination {
                group {
                    network-group PRIVATE_NETS
                }
            }
            modify {
                table main
            }
        }
        rule 90 {
            action modify
            description "do NOT load balance destination public address"
            destination {
                group {
                    address-group ADDRv4_pppoe7
                }
            }
            modify {
                table main
            }
        }
        rule 100 {
            action modify
            description "do NOT load balance destination public address"
            destination {
                group {
                    address-group ADDRv4_pppoe8
                }
            }
            modify {
                table main
            }
        }
        rule 110 {
            action modify
            description "do NOT load balance destination public address"
            destination {
                group {
                    address-group ADDRv4_pppoe9
                }
            }
            modify {
                table main
            }
        }
	rule 130 {
            action modify
            modify {
                lb-group ketoan
            }
            source {
                group {
                    address-group fwketoan
                }
            }
        }
        rule 140 {
            action modify
            modify {
                lb-group G
            }
        }
    }
    name WAN_IN {
        default-action drop
        description "WAN to internal"
        rule 10 {
            action accept
            description "Allow established/related"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
    }
    name WAN_LOCAL {
        default-action drop
        description "WAN to router"
        rule 10 {
            action accept
            description "Allow established/related"
            state {
                established enable
                related enable
            }
        }
        rule 20 {
            action accept
            description remote
            destination {
                port 443,22
            }
            log disable
            protocol tcp
        }
        rule 30 {
            action drop
            description "Drop invalid state"
            state {
                invalid enable
            }
        }
    }
    options {
        mss-clamp {
            mss 1412
        }
    }
    receive-redirects disable
    send-redirects enable
    source-validation disable
    syn-cookies enable
}
interfaces {
    ethernet eth0 {
        duplex auto
        speed auto
    }
    ethernet eth1 {
        duplex auto
        speed auto
    }
    ethernet eth2 {
        duplex auto
        speed auto
    }
    ethernet eth3 {
        duplex auto
        speed auto
    }
    ethernet eth4 {
        duplex auto
        speed auto
    }
    ethernet eth5 {
        duplex auto
        speed auto
    }
    ethernet eth6 {
        duplex auto
        speed auto
    }
    ethernet eth7 {
        description WAN
        duplex auto
        pppoe 7 {
            default-route auto
            firewall {
                in {
                    name WAN_IN
                }
                local {
                    name WAN_LOCAL
                }
            }
            mtu 1492
            name-server auto
            password a171ed98
            user-id bds1ankhang
        }
        speed auto
    }
    ethernet eth8 {
        description "WAN 2"
        duplex auto
        pppoe 8 {
            default-route auto
            firewall {
                in {
                    name WAN_IN
                }
                local {
                    name WAN_LOCAL
                }
            }
            mtu 1492
            name-server auto
            password 14b377b9
            user-id bds2ankhang
        }
        speed auto
    }
    ethernet eth9 {
        description "WAN 3"
        duplex auto
        pppoe 9 {
            default-route auto
            firewall {
                in {
                    name WAN_IN
                }
                local {
                    name WAN_LOCAL
                }
            }
            mtu 1492
            name-server auto
            password 0U9OH0
            user-id t008_gftth_khangctcptvvdt0
        }
        speed auto
    }
    ethernet eth10 {
        duplex auto
        speed auto
    }
    ethernet eth11 {
        duplex auto
        speed auto
    }
    switch switch0 {
        address 192.168.10.1/23
        description Local
        firewall {
            in {
                modify balance
            }
        }
        mtu 1500
        switch-port {
            interface eth0 {
            }
            interface eth1 {
            }
            interface eth2 {
            }
            interface eth3 {
            }
            interface eth4 {
            }
            interface eth5 {
            }
            interface eth6 {
            }
            vlan-aware disable
        }
        vif 2 {
            address 192.168.2.1/24
            description VLAN2
            mtu 1500
        }
    }
}
load-balance {
    group G {
        exclude-local-dns disable
        flush-on-active enable
        gateway-update-interval 20
        interface pppoe7 {
        }
        interface pppoe8 {
        }
        interface pppoe9 {
        }
        lb-local enable
        lb-local-metric-change disable
    }
	group ketoan {
        interface pppoe7 {
        }
        interface pppoe9 {
            failover-only
        }
        lb-local enable
        lb-local-metric-change disable
    }
}
port-forward {
    auto-firewall enable
    hairpin-nat enable
    lan-interface switch0
    rule 1 {
        description Cam
        forward-to {
            address 192.168.10.10
            port 8080
        }
        original-port 8080
        protocol tcp
    }
    rule 2 {
        description cam1
        forward-to {
            address 192.168.10.10
            port 8000
        }
        original-port 8000
        protocol tcp
    }
    wan-interface pppoe7
}
service {
    dhcp-server {
        disabled false
        hostfile-update disable
        shared-network-name LAN {
            authoritative enable
            subnet 192.168.10.0/23 {
                default-router 192.168.10.1
                dns-server 8.8.8.8
                dns-server 8.8.4.4
                lease 86400
                start 192.168.10.20 {
                    stop 192.168.11.254
                }
		static-mapping DESKTOP-8M2NJ75 {
                    ip-address 192.168.10.228
                    mac-address 94:c6:91:97:d0:64
		}
            }
        }
        shared-network-name Vlan2 {
            authoritative disable
            subnet 192.168.2.0/24 {
                default-router 192.168.2.1
                dns-server 8.8.8.8
                dns-server 8.8.4.4
                lease 86400
                start 192.168.2.2 {
                    stop 192.168.2.254
                }
            }
        }
        static-arp disable
        use-dnsmasq disable
    }
    dns {
        forwarding {
            cache-size 150
            listen-on switch0
        }
    }
    gui {
        http-port 80
        https-port 443
        older-ciphers enable
    }
    nat {
        rule 5014 {
            description "masquerade for WAN"
            outbound-interface pppoe7
            type masquerade
        }
        rule 5016 {
            description "masquerade for WAN 2"
            outbound-interface pppoe8
            type masquerade
        }
        rule 5018 {
            description "masquerade for WAN 3"
            outbound-interface pppoe9
            type masquerade
        }
    }
    ssh {
        port 22
        protocol-version v2
    }
    unms {
    }
}
system {
    conntrack {
        expect-table-size 4096
        hash-size 4096
        table-size 32768
        tcp {
            half-open-connections 512
            loose enable
            max-retrans 3
        }
    }
    host-name ubnt
    login {
        user ubnt {
            authentication {
                encrypted-password $6$CSW04YyRK3KE1Uc3$FJZ2cWp7KtAZFniampRyJPXlmn.6Rt49cM4oUKm2vPdwcN62AvH465/Y/nRLqgSXmYosMStRf5IHCnDnbLIz.1
            }
            level admin
        }
    }
    ntp {
        server 0.ubnt.pool.ntp.org {
        }
        server 1.ubnt.pool.ntp.org {
        }
        server 2.ubnt.pool.ntp.org {
        }
        server 3.ubnt.pool.ntp.org {
        }
    }
    offload {
        hwnat disable
        ipv4 {
            forwarding enable
            pppoe enable
        }
    }
    syslog {
        global {
            facility all {
                level notice
            }
            facility protocols {
                level debug
            }
        }
    }
    time-zone UTC
}
vpn {
    pptp {
        remote-access {
            authentication {
                local-users {
                    username dat {
                        password dat12345
                    }
                }
                mode local
            }
            client-ip-pool {
                start 192.168.10.252
                stop 192.168.10.253
            }
            dns-servers {
                server-1 8.8.8.8
            }
            mtu 1480
            outside-address 113.161.35.88
        }
    }
}

/* Warning: Do not remove the following line. */
/* === vyatta-config-version: "config-management@1:conntrack@1:cron@1:dhcp-relay@1:dhcp-server@4:firewall@5:ipsec@5:nat@3:qos@1:quagga@2:suspend@1:system@5:ubnt-l2tp@1:ubnt-pptp@1:ubnt-udapi-server@1:ubnt-unms@2:ubnt-util@1:vrrp@1:vyatta-netflow@1:webgui@1:webproxy@1:zone-policy@1" === */
/* Release version: v2.0.9.5346345.201028.1647 */
