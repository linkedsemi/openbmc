LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=8c16666ae6c159876a0ba63099614381"

BRANCH = "master"
SRC_URI = "git://github.com/cpb-/spi-tools.git;branch=${BRANCH};protocol=https"
SRCREV = "bb75d4690edc123524ff12c29a24f90d4c48ee01"

S = "${WORKDIR}/git"
inherit autotools
RDEPENDS:${PN} += "bash"
