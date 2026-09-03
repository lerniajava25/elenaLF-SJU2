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
    }

    @Override
    public Color shade(Vector3D point, Vector3D normal, Scene scene) {
        Color result = albedo.multiply(scene.ambient());

        for (PointLight light : scene.lights()) {
            if (scene.isOccluded(point, light.position())) {
                continue; // something blocks this light -> shadow
            }
            Vector3D lightDir = light.position().subtract(point).normalize();
            double cosine = Math.max(0.0, normal.dot(lightDir)); //l = cos 0/degres
            if (cosine == 0.0) { // 90 degrees
                continue;
            }
            result = result.add(
                    albedo.multiply(light.color()).scale(cosine * light.intensity()));
        }
        return result;
    }
}
