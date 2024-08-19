
SUMMARY = "trzsz is a simple file transfer tools, similar to lrzsz ( rz / sz ) and compatible with tmux, which works with iTerm2 and has a nice progress bar."
HOMEPAGE = "https://trzsz.github.io"
AUTHOR = "Lonny Wong <lonnywong@qq.com>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://setup.py;md5=0e50d41254de9d7b093e9d6d679a20f1"

SRC_URI = "https://files.pythonhosted.org/packages/f2/c2/89cfeb038585c18e320ede2182d70096a162f22298e29b7f1234bbc5230e/trzsz-libs-1.1.5.tar.gz"
SRC_URI[md5sum] = "75c4826ce27c25ebf8b5107f55118e68"
SRC_URI[sha256sum] = "baff5cea450e1310a292f5702d4a8f7dc855fbe2aefe21b13d2451bc05cedea4"

S = "${WORKDIR}/trzsz-libs-1.1.5"

RDEPENDS_${PN} = ""

inherit setuptools3
