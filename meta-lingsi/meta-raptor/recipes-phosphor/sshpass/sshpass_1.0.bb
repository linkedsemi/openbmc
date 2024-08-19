LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/AUTHORS;md5=0c756c8a2c0c9d79ae82fbfce98ca153"

BRANCH = "main"
SRC_URI = "git://git.code.sf.net/p/sshpass/code-git;branch=${BRANCH};protocol=https"
SRCREV = "ef1034d5f0b31fee6f27dce15a47a90f08935622"

S = "${WORKDIR}/git"
inherit autotools
RDEPENDS:${PN} += "bash"
