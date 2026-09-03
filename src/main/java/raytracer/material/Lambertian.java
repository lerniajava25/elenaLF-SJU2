package raytracer.material;

import raytracer.scene.PointLight;
import raytracer.math.Color;
import raytracer.math.Vector3D;
import raytracer.scene.Scene;

//Lembert's cosine law:  Surface law[https://www.youtube.com/watch?v=0zmLe4SssJc]
//A matte, diffuse surface following Lambert's cosine law: brightness depends on the angle between the surface normal and each light. For every light it skips the ones hidden in shadow, weights by max(0, normal · lightDir), and tints the light by the surface albedo(brightness). A small ambient(surrounding lights) term keeps shadows from going pure black.


//length *normal  = cos theta
public final class Lambertian implements Material {

    private final Color albedo; //  albedo:Latin word for whiteness

    public Lambertian(Color albedo) {
        this.albedo = albedo;
    } // here start with the solidColor

    @Override
    public Color shade(Vector3D point, Vector3D normal, Scene scene) {
        Color result = albedo.multiply(scene.ambient()); //start with the ambient light the starting color maybe a week light

        // go through every light: For every light in the scene, calculate how much that light contributes to this point.
        for (PointLight light : scene.lights()) {
            if (scene.isOccluded(point, light.position())) {
                continue; // Check whether the point is in shadow or something blocks this light -> shadow
            }
            Vector3D lightDir = light.position().subtract(point).normalize(); //here i care about the direction so changes the vector's length to 1 while keeping its direction. Calculate how directly the light hits the surface, but never allow the brightness to go below zero.
            double cosine = Math.max(0.0, normal.dot(lightDir)); //l = cos 0/degres = 1 , means it hits the object directly
            if (cosine == 0.0) { // cos 90 degrees = 0 light arrow fron the side so it doesn't hitt the surface
                continue;
            }
            result = result.add(
                    albedo.multiply(light.color()).scale(cosine * light.intensity()));
        }
        return result;
    }
}

//        Start with a little ambient light.
//
//        For every lamp:
//        Is the lamp blocked?
//        YES → ignore it.
//
//        Find the direction from the point to the lamp.
//
//        Compare that direction with the surface normal.
//
//        Is the lamp behind / beside the surface?
//        YES → ignore it.
//
//        Calculate:
//        object color
//        × light color
//        × angle brightness
//        × light intensity
//
//        Add that light to the final color.
//
//        Return the final color.