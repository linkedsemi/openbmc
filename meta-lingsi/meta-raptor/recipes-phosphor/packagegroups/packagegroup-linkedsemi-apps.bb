SUMMARY = "OpenBMC for linkedsemi Applications"
PR = "r1"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = " \
        ${PN}-system \
        ${PN}-chassis \
        "

PROVIDES += "virtual/obmc-system-mgmt"
PROVIDES += "virtual/obmc-chassis-mgmt"

RPROVIDES:${PN}-system += "virtual-obmc-system-mgmt"
RPROVIDES:${PN}-chassis += "virtual-obmc-chassis-mgmt"

SUMMARY_${PN}-system = "raptor System"
RDEPENDS:${PN}-system = " \
        "
# RDEPENDS:${PN}-system = " \
#         phosphor-ipmi-kcs \
#         "

SUMMARY:${PN}-chassis = "raptor Chassis"
RDEPENDS:${PN}-chassis = " \
        "
# RDEPENDS:${PN}-chassis = " \
#         x86-power-control \
#         "
