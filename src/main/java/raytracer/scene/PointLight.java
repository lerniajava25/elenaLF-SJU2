package raytracer.scene;

import raytracer.math.Color;
import raytracer.math.Vector3D;

//A point light: radiates equally in all directions from one position.
public record PointLight(Vector3D position, Color color, double intensity) {
    public PointLight(Vector3D position, Color color) {
        this(position, color, 1.0);
    }
}
