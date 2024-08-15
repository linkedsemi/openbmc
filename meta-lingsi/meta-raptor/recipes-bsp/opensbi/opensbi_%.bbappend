FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-unset-semihost_v1.4.patch"

# EXTRA_OEMAKE += " FW_JUMP=n FW_PAYLOAD=n DEBUG=1 "
EXTRA_OEMAKE += " FW_JUMP=n FW_PAYLOAD=n "
