package raytracer.material;

import raytracer.math.Color;
import raytracer.math.Vector3D;
import raytracer.scene.Scene;

// this is an interface to define what must exist.
public interface Material {
//point: the 3d position on the object's surface/ where did the ray hit?
    //normal: which direction the surface is facing// important for lighting later on if the surface face the light or not
    //scene: gives the material access to information about the whole scene./ where is the light

    Color shade(Vector3D point, Vector3D normal, Scene scene);
}
