/*
 * Copyright (c) 2008-2009, Matthias Mann
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Matthias Mann nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.matthiasmann.twl.renderer.lwjgl;

import de.matthiasmann.twl.renderer.MouseCursor;
import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

/**
 *
 * @author Matthias Mann
 */
class LWJGLCursor implements MouseCursor {

    long glfwCursor;

    LWJGLCursor(ByteBuffer src, LWJGLTexture.Format srcFmt, int srcStride,
            int x, int y, int width, int height, int hotSpotX, int hotSpotY) {
        int dstSize = Math.max( Math.max(width, height), 4 );

        //capacity*4 for each r,g,b,a byte
        ByteBuffer buf = BufferUtils.createByteBuffer(dstSize*dstSize*4);
        for(int row=height,dstPos=0 ; row-->0 ; dstPos+=dstSize) {
            int offset = srcStride * (y+row) + x * srcFmt.getPixelSize();
            buf.position(dstPos);

            switch(srcFmt) {
            case RGB:
                for(byte col=0 ; col<width ; col++) {
                    buf.put(src.get(offset + col*3 + 0));
                    buf.put(src.get(offset + col*3 + 1));
                    buf.put(src.get(offset + col*3 + 2));
                    buf.put((byte)255);
                }
                break;
            case RGBA:
                for(byte col=0 ; col<width ; col++) {
                    buf.put(src.get(offset + col*4 + 0));
                    buf.put(src.get(offset + col*4 + 1));
                    buf.put(src.get(offset + col*4 + 2));
                    buf.put(src.get(offset + col*4 + 3));
                }
                break;
            case ABGR:
                for(byte col=0 ; col<width ; col++) {
                    buf.put(src.get(offset + col*4 + 3));
                    buf.put(src.get(offset + col*4 + 2));
                    buf.put(src.get(offset + col*4 + 1));
                    buf.put(src.get(offset + col*4 + 0));
                }
                break;
            default:
                throw new IllegalStateException("Unsupported color format");
            }
        }
        buf.clear();
        
        glfwCursor = GLFW.glfwCreateCursor(buf,hotSpotX,hotSpotY);
        
    }

    //Will be destroyed by glfwTerminate
    void destroy() {
        //GLFW.glfwDestroyCursor(glfwCursor);
    }

}
