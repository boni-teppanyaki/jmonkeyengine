/*
 * Copyright (c) 2009-2021 jMonkeyEngine
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
package com.jme3.scene.debug;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

/**
 * Factory helpers for common scene debug overlays (ground grid, axis triad).
 *
 * @author jMonkeyEngine contributors
 */
public final class DebugScene {

    private static final int DEFAULT_DIVISIONS = 20;
    private static final float DEFAULT_SPACING = 1f;
    private static final float DEFAULT_AXIS_LENGTH = 2f;

    private DebugScene() {
    }

    /**
     * Attaches a centered ground grid on the XZ plane (Y = 0) with default size
     * ({@value #DEFAULT_DIVISIONS} divisions, {@value #DEFAULT_SPACING} unit spacing).
     *
     * @param parent the node to attach to (not null)
     * @param assets the asset manager (not null)
     * @return the grid geometry (not null)
     */
    public static Geometry attachGroundGrid(Node parent, AssetManager assets) {
        return attachGroundGrid(parent, assets, DEFAULT_DIVISIONS, DEFAULT_SPACING);
    }

    /**
     * Attaches a centered ground grid on the XZ plane (Y = 0).
     *
     * @param parent the node to attach to (not null)
     * @param assets the asset manager (not null)
     * @param divisions number of cells along each axis (must be &gt; 0)
     * @param spacing distance between grid lines in world units (must be &gt; 0)
     * @return the grid geometry (not null)
     */
    public static Geometry attachGroundGrid(Node parent, AssetManager assets,
            int divisions, float spacing) {
        if (divisions <= 0) {
            throw new IllegalArgumentException("divisions must be positive");
        }
        if (spacing <= 0f) {
            throw new IllegalArgumentException("spacing must be positive");
        }
        Geometry grid = lineShape(assets, new Grid(divisions + 1, divisions + 1, spacing),
                ColorRGBA.Gray, "DebugGroundGrid");
        grid.center();
        parent.attachChild(grid);
        return grid;
    }

    /**
     * Attaches RGB X/Y/Z axis arrows at the origin with default length
     * ({@value #DEFAULT_AXIS_LENGTH}).
     *
     * @param parent the node to attach to (not null)
     * @param assets the asset manager (not null)
     * @return a node containing the three axis geometries (not null)
     */
    public static Node attachAxisTriad(Node parent, AssetManager assets) {
        return attachAxisTriad(parent, assets, DEFAULT_AXIS_LENGTH);
    }

    /**
     * Attaches RGB X/Y/Z axis arrows at the origin.
     *
     * @param parent the node to attach to (not null)
     * @param assets the asset manager (not null)
     * @param length arrow length in world units (must be &gt; 0)
     * @return a node containing the three axis geometries (not null)
     */
    public static Node attachAxisTriad(Node parent, AssetManager assets, float length) {
        if (length <= 0f) {
            throw new IllegalArgumentException("length must be positive");
        }
        Node axes = new Node("DebugAxisTriad");
        axes.attachChild(lineShape(assets, new Arrow(Vector3f.UNIT_X.mult(length)),
                ColorRGBA.Red, "DebugAxisX"));
        axes.attachChild(lineShape(assets, new Arrow(Vector3f.UNIT_Y.mult(length)),
                ColorRGBA.Green, "DebugAxisY"));
        axes.attachChild(lineShape(assets, new Arrow(Vector3f.UNIT_Z.mult(length)),
                ColorRGBA.Blue, "DebugAxisZ"));
        parent.attachChild(axes);
        return axes;
    }

    /**
     * Attaches a default ground grid and axis triad.
     *
     * @param parent the node to attach to (not null)
     * @param assets the asset manager (not null)
     */
    public static void attachDefaults(Node parent, AssetManager assets) {
        attachGroundGrid(parent, assets);
        attachAxisTriad(parent, assets);
    }

    /**
     * Attaches a ground grid and axis triad with custom dimensions.
     *
     * @param parent the node to attach to (not null)
     * @param assets the asset manager (not null)
     * @param divisions number of grid cells along each axis (must be &gt; 0)
     * @param spacing grid line spacing in world units (must be &gt; 0)
     * @param axisLength axis arrow length in world units (must be &gt; 0)
     */
    public static void attachDefaults(Node parent, AssetManager assets,
            int divisions, float spacing, float axisLength) {
        attachGroundGrid(parent, assets, divisions, spacing);
        attachAxisTriad(parent, assets, axisLength);
    }

    private static Geometry lineShape(AssetManager assets, Mesh mesh,
            ColorRGBA color, String name) {
        Geometry geo = new Geometry(name, mesh);
        Material mat = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geo.setMaterial(mat);
        return geo;
    }
}
