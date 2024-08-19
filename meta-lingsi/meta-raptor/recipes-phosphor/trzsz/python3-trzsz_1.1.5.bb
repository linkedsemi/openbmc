
SUMMARY = "trzsz is a simple file transfer tools, similar to lrzsz ( rz / sz ) and compatible with tmux, which works with iTerm2 and has a nice progress bar."
HOMEPAGE = "https://trzsz.github.io"
AUTHOR = "Lonny Wong <lonnywong@qq.com>"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE.md;md5=593defa65b83328b759391f19e146f2f"

SRC_URI = "https://files.pythonhosted.org/packages/22/1e/40a495c84a0dc625a4d97638c5cae308306718c493f480ee5ac64801947b/trzsz-1.1.5.tar.gz"
SRC_URI[md5sum] = "9357bc690e3f20173e18ee525ad1d064"
SRC_URI[sha256sum] = "57be064b259d57326f75683704b8e93a56ce0d67d9b3b2b36ad4d53e98a28854"

S = "${WORKDIR}/trzsz-1.1.5"

RDEPENDS_${PN} = "python3-trzsz-libs python3-trzsz-svr"

inherit setuptools3
