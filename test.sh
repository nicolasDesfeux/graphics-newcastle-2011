#!/bin/sh
java -Djava.library.path=./native/linux -cp .:jar/lwjgl.jar:jar/lwjgl_util.jar:jar/lwjgl-debug.jar:jar/lwjgl_test.jar:jar/lwjgl_util.jar:jar/lwjgl_util_applet.jar:jar/lzma.jar:jar/slick-util.jar:jar/jinput.jar:test.jar  src.SnakeGame
