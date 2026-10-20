require recipes-bsp/u-boot/u-boot-common.inc
require recipes-bsp/u-boot/u-boot.inc

SUMMARY = "U-Boot for LinkedSemi LS2010"
PROVIDES += "u-boot"

# Override oe-core defaults AFTER the includes
BRANCH = "linkedsemi"
SRC_URI = "git://${LINKEDSEMI_GIT}/u-boot.git;protocol=http;branch=${BRANCH}"
SRCREV = "2af2af9428e4eed725366d8cfa3369024ab8b5d1"
PV = "v2026.10+git"

DEPENDS += "bc-native dtc-native swig-native"
