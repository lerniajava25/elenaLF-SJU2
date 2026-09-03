package raytracer.material;

import raytracer.math.Color;
import raytracer.math.Vector3D;
import raytracer.scene.Scene;

// A flat, unlit material(Matte): always the same color, ignoring lights and shadows. Next to Lambertian it makes polymorphism visible ,  a SolidColor sphere looks like a flat disc because it has no shading gradient.
//so here everypixel has the same color, no lightning no shadows, it will look lika a flat circle then it will be a 3D ball
// implements the Material interface-> 1- Solid Color: always the same color(no lights in the shade) 2- lambertian (with lights in the shade)
public final class SolidColor implements Material {

    private final Color color;

    public SolidColor(Color color) {
        this.color = color;
    }

    @Override
    public Color shade(Vector3D point, Vector3D normal, Scene scene) {
        return color;
    }
}
