require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bc-native dtc-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

#v2024.01
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
SRCREV = "9e53e45292ee2f1d9d2ccc59914b161bef9b10d7"

# DEPENDS:append = " u-boot-tools-native"

COMPATIBLE_MACHINE = "(raptor)"

TOOLCHAIN = "gcc"
