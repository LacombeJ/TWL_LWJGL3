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
package keyevents;

import de.matthiasmann.twl.Container;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ScrollPane;
import de.matthiasmann.twl.ScrollPane.Fixed;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.theme.ThemeManager;

import org.lacombej.test.Window;
import org.lacombej.twl.TLC;
import org.lwjgl.opengl.GL11;
import test.TestUtils;

/**
 *
 * @author Matthias Mann
 */
public class ListKeyEvents extends Container {

    static Window window;
    
    public static void main(String[] args) {
        try {
            window = new Window("TWL Keyboard Event Debugger");

            TLC.create(window.id);
            
            LWJGLRenderer renderer = new LWJGLRenderer();
            ListKeyEvents demo = new ListKeyEvents();
            GUI gui = new GUI(demo, renderer);

            ThemeManager theme = ThemeManager.createThemeManager(
                    ListKeyEvents.class.getResource("ListKeyEvents.xml"), renderer);
            gui.applyTheme(theme);

            while(window.isRunning() && !demo.quit) {
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
                TLC.update();
                demo.listKeyEvents();
                gui.update();
                window.update();
            }

            gui.destroy();
            theme.destroy();
        } catch (Exception ex) {
            TestUtils.showErrMsg(ex);
        }
        window.destroy();
    }

    private final StringBuilder sb;
    private final SimpleTextAreaModel textAreaModel;
    private final TextArea textArea;
    private final ScrollPane scrollPane;

    public boolean quit;

    public ListKeyEvents() {
        sb = new StringBuilder();
        sb.append("Press any key - the keyboard events are displayed below\n");
        
        textAreaModel = new SimpleTextAreaModel(sb.toString());
        textArea = new TextArea(textAreaModel);
        scrollPane = new ScrollPane(textArea);
        scrollPane.setFixed(Fixed.HORIZONTAL);
        
        add(scrollPane);
    }
    
    public void listKeyEvents() {
        boolean hadEvents = false;
        while(TLC.keyboard().next()) {
            if(TLC.keyboard().getEventCharacter() != Event.CHAR_NONE) {
                sb.append(String.format("%s %s (code %d) char %c (%d)\n",
                        TLC.keyboard().getEventKeyState() ? "PRESSED " : "RELEASED",
                        TLC.keyboard().getKeyName(TLC.keyboard().getEventKey()),
                        TLC.keyboard().getEventKey(),
                        TLC.keyboard().getEventCharacter(),
                        (int)TLC.keyboard().getEventCharacter()));
            } else {
                sb.append(String.format("%s %s (code %d)\n",
                        TLC.keyboard().getEventKeyState() ? "PRESSED " : "RELEASED",
                        TLC.keyboard().getKeyName(TLC.keyboard().getEventKey()),
                        TLC.keyboard().getEventKey()));
            }
            hadEvents = true;
        }
        if(hadEvents) {
            boolean atEnd = scrollPane.getScrollPositionY() == scrollPane.getMaxScrollPosY();
            textAreaModel.setText(sb.toString());
            if(atEnd) {
                scrollPane.validateLayout();
                scrollPane.setScrollPositionY(scrollPane.getMaxScrollPosY());
            }
        }
    }
    
}
