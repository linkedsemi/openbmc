SUMMARY = "OpenBMC for linkedsemi Applications"
PR = "r1"

inherit packagegroup

PROVIDES = "${PACKAGES}"
PACKAGES = " \
        ${PN}-system \
        "

PROVIDES += "virtual/obmc-system-mgmt"

RPROVIDES:${PN}-system += "\
        virtual-obmc-system-mgmt \
        "

SUMMARY_${PN}-system = "raptor System"
