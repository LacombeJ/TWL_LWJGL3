/*
 * Copyright (c) 2008-2012, Matthias Mann
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
package test;

import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;
import de.matthiasmann.twl.CallbackWithReason;
import de.matthiasmann.twl.Container;
import de.matthiasmann.twl.GUI;

import org.lacombej.test.Window;
import org.lacombej.twl.TLC;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Matthias Mann
 */
public class ModeSelectTest {

    public static void main(String ... args) {
        ModeSelectTest modeSel = new ModeSelectTest();
        VideoMode mode = modeSel.selectMode();
        if(mode != null) {
            SimpleTest test = new SimpleTest();
            test.run(mode);
        }
    }

    protected VideoSettings.CallbackReason reason;

    public  ModeSelectTest() {
        System.out.println("LWJGL Version: " + Sys.getVersion());
        
    }

    public Window window;
    
    public VideoMode selectMode() {
        try {
            window = new Window(400,300);

            TLC.create(window.id);
            
            LWJGLRenderer renderer = new LWJGLRenderer();
            renderer.setUseSWMouseCursors(true);
            GUI gui = new GUI(renderer);

            ThemeManager theme = ThemeManager.createThemeManager(
                    SimpleTest.class.getResource("guiTheme.xml"), renderer);
            gui.applyTheme(theme);

            Container frame = new Container();
            
            frame.setTheme("settingdialog");

            gui.getRootPane().add(frame);
            frame.adjustSize();
            frame.setPosition(
                    (gui.getWidth()-frame.getWidth())/2,
                    (gui.getHeight()-frame.getHeight())/2);

            while(window.isRunning() && reason == null) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

                gui.update();
                window.update();
                TestUtils.reduceInputLag();
            }

            gui.destroy();
            theme.destroy();
            window.destroy();
            
        } catch (Exception ex) {
            TestUtils.showErrMsg(ex);
        }
        
        return null;
    }
}
