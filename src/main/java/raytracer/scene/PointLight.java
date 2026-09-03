package raytracer.scene;

import raytracer.math.Color;
import raytracer.math.Vector3D;

//A point light:  where does the light coming from:where am i ? what color am i? how strong am i ?  radiates equally in all directions from one position.
public record PointLight(Vector3D position, Color color, double intensity) {
    public PointLight(Vector3D position, Color color) {
        // to specify the intensity
        this(position, color, 1.0);
    }
}
