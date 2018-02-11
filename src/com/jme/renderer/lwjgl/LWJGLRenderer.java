/*
 * Copyright (c) 2003-2007 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme.renderer.lwjgl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.ARBBufferObject;
import org.lwjgl.opengl.ARBMultitexture;
import org.lwjgl.opengl.ARBVertexBufferObject;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.EXTCompiledVertexArray;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.OpenGLException;


import org.lwjgl.util.glu.GLU;


import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;

import com.jme.renderer.Renderer;

import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;


/**
 * <code>LWJGLRenderer</code> provides an implementation of the
 * <code>Renderer</code> interface using the LWJGL API.
 *
 * @see com.jme.renderer.Renderer
 * @author Mark Powell - initial implementation, and more.
 * @author Joshua Slack - Further work, Optimizations, Headless rendering
 * @author Tijl Houtbeckers - Small optimizations and improved VBO
 * @version $Id: LWJGLRenderer.java,v 1.148 2008/01/08 16:42:19 renanse Exp $
 */

public class LWJGLRenderer extends Renderer {

    private static final Logger logger = Logger.getLogger(LWJGLRenderer.class.getName());
 

    private boolean supportsVBO = false;
 
    private boolean inOrthoMode;
 
    
 
    protected ContextCapabilities capabilities;
 

    /**
     * Constructor instantiates a new
     * <code>LWJGLRenderer</code> object. The size of the rendering window is
     * passed during construction.
     *
     * @param width the width of the rendering context.
     * @param height the height of the rendering context.
     */
    public LWJGLRenderer(int width, int height) {
        if (width <= 0 || height <= 0) {
            logger.warning("Invalid width and/or height values.");
            throw new JmeException("Invalid width and/or height values.");
        }
        this.width = width;
        this.height = height;

        logger.info("LWJGLRenderer created. W:  " + width + "H: " + height);

        capabilities = GLContext.getCapabilities();

   
  
    }

    /**
     * Reinitialize the renderer with the given width/height. Also calls resize
     * on the attached camera if present.
     *
     * @param width int
     * @param height int
     */
    public void reinit(int width, int height) {
        if (width <= 0 || height <= 0) {
            logger.warning("Invalid width and/or height values.");
            throw new JmeException("Invalid width and/or height values.");
        }
        this.width = width;
        this.height = height;
        if (camera != null) {
            camera.resize(width, height);
            camera.apply();
        }
        capabilities = GLContext.getCapabilities();
    }

    /**
     * <code>setCamera</code> sets the camera this renderer is using. It asserts
     * that the camera is of type
     * <code>LWJGLCamera</code>.
     *
     * @see com.jme.renderer.Renderer#setCamera(com.jme.renderer.Camera)
     */
    public void setCamera(Camera camera) {
        if (camera instanceof LWJGLCamera) {
            this.camera = (LWJGLCamera) camera;
        }
    }

    /**
     * <code>createCamera</code> returns a default camera for use with the LWJGL
     * renderer.
     *
     * @param width the width of the frame.
     * @param height the height of the frame.
     * @return a default LWJGL camera.
     */
    public Camera createCamera(int width, int height) {
        return new LWJGLCamera(width, height, this);
    }

    



  
    public boolean takeScreenShot(String filename) {
        if (null == filename) {
            throw new JmeException("Screenshot filename cannot be null");
        }
        logger.info("Taking screenshot: " + filename + ".png");

        // Create a pointer to the image info and create a buffered image to
        // hold it.
        IntBuffer buff = ByteBuffer.allocateDirect(width * height * 4).order(
                ByteOrder.LITTLE_ENDIAN).asIntBuffer();
        grabScreenContents(buff, 0, 0, width, height);
        BufferedImage img = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        // Grab each pixel information and set it to the BufferedImage info.
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                img.setRGB(x, y, buff.get((height - y - 1) * width + x));
            }
        }

        // write out the screenshot image to a file.
        try {
            File out = new File(filename + ".png");
            return ImageIO.write(img, "png", out);
        } catch (IOException e) {
            logger.warning("Could not create file: " + filename + ".png");
            return false;
        }
    }

    /**
     * <code>grabScreenContents</code> reads a block of pixels from the current
     * framebuffer.
     *
     * @param buff a buffer to store contents in.
     * @param x - x starting point of block
     * @param y - y starting point of block
     * @param w - width of block
     * @param h - height of block
     */
    public void grabScreenContents(IntBuffer buff, int x, int y, int w, int h) {
        GL11.glReadPixels(x, y, w, h, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE,
                buff);
    }

    /**
     * <code>draw</code> renders a curve object.
     *
     * @param c the curve object to render.
     */
  

    /**
     * <code>draw</code> renders a
     * <code>LineBatch</code> object including it's normals, colors, textures
     * and vertices.
     *
     * @see Renderer#draw(LineBatch)
     * @param batch the lines to render.
     */
    

    /**
     * <code>draw</code> renders a
     * <code>PointBatch</code> object including it's normals, colors, textures
     * and vertices.
     *
     * @see Renderer#draw(PointBatch)
     * @param batch the points to render.
     */
   
    /**
     * <code>draw</code> renders a
     * <code>QuadBatch</code> object including it's normals, colors, textures
     * and vertices.
     *
     * @see Renderer#draw(QuadBatch)
     * @param batch the mesh to render.
     */
   

    /**
     * <code>draw</code> renders a
     * <code>TriMesh</code> object including it's normals, colors, textures and
     * vertices.
     *
     * @see Renderer#draw(TriangleBatch)
     * @param batch the mesh to render.
     */
   

   

    /**
     * <code>prepVBO</code> binds the geometry data to a vbo buffer and sends it
     * to the GPU if necessary. The vbo id is stored in the geometry's VBOInfo
     * class. If a new vbo id is created, the VBO is also stored in a cache.
     * Before creating a new VBO this cache will be checked to see if a VBO is
     * already created for that Buffer.
     *
     * @param g the geometry to initialize VBO for.
     */
  
    /**
     * <code>draw</code> renders a scene by calling the nodes
     * <code>onDraw</code> method.
     *
     * @see com.jme.renderer.Renderer#draw(com.jme.scene.Spatial)
     */


    /**
     * <code>draw</code> renders a text object using a predefined font.
     *
     * @see com.jme.renderer.Renderer#draw(com.jme.scene.Text)
     */
  

    /**
     * Return true if the system running this supports VBO
     *
     * @return boolean true if VBO supported
     */
    public boolean supportsVBO() {
        return supportsVBO;
    }

    /**
     * re-initializes the GL context for rendering of another piece of geometry.
     */
 

    /**
     * <code>flush</code> tells opengl to send through all currently waiting
     * commands in the buffer.
     */
    public void flush() {
        GL11.glFlush();
    }

    /**
     * <code>finish</code> is similar to flush, however it blocks until all
     * waiting OpenGL commands have been finished.
     */
    public void finish() {
        GL11.glFinish();
    }

    /**
     * Prepares the GL Context for rendering this geometry. This involves
     * initializing the VBO and obtaining the buffer data.
     *
     * @param t the geometry to process.
     * @return true if VBO is used for indicis, false if not
     */
  

    

    // inherited documentation
    public void releaseDisplayList(int listId) {
        GL11.glDeleteLists(listId, 1);
    }

    // inherited documentation
    public void setPolygonOffset(float factor, float offset) {
        GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
        GL11.glPolygonOffset(factor, offset);
    }

    // inherited documentation
    public void clearPolygonOffset() {
        GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
    }



 



   

    @Override
    public void checkCardError() throws JmeException {
        try {
            org.lwjgl.opengl.Util.checkGLError();
        } catch (OpenGLException exception) {
            throw new JmeException("Error in opengl: " + exception.getMessage(), exception);
        }
    }

  
}
