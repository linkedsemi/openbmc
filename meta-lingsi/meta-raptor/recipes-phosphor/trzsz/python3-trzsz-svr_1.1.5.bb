
SUMMARY = "trzsz is a simple file transfer tools, similar to lrzsz ( rz / sz ) and compatible with tmux, which works with iTerm2 and has a nice progress bar."
HOMEPAGE = "https://trzsz.github.io"
AUTHOR = "Lonny Wong <lonnywong@qq.com>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://setup.py;md5=00c9c6cb45d0d9cdfcc31f6ba639be28"

SRC_URI = "https://files.pythonhosted.org/packages/76/c7/78c1c91eeb99c86dd80d903cdb463da0d1fbea9b7f25a1c1de5b0f96ecf5/trzsz-svr-1.1.5.tar.gz"
SRC_URI[md5sum] = "ec1456d163a9aea6238915b2e0c08072"
SRC_URI[sha256sum] = "2f1fbc119df3c6010bf7b030635a5dc3cc1513025e5d0415d84d2d2a417b077f"

S = "${WORKDIR}/trzsz-svr-1.1.5"

RDEPENDS_${PN} = "python3-trzsz-libs"

inherit setuptools3
