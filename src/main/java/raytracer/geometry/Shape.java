package raytracer.geometry;

import raytracer.material.Material;  // what the surface looks like
import raytracer.math.Ray; //a line travelling through the scene
import raytracer.math.Vector3D; // a 3d direction/position

import java.util.Optional;

// an abstract class is a parent/ template for all shapes/objects. including here the shared characteristics:
// 1- material 2-every shape must know if a ray hits it or not 3- surface normals

public abstract class Shape {
    private final Material material;

    //This constructor is protected, it  means subclasses such as Sphere can use it and because Shape isn't something you want to instantiate directly
    protected Shape(Material material) {
        this.material = material;
    }

    // now check if the ray hits the shape
    //Returns the hit details, or empty on a miss. Each subclass will override this.
    // there are no calculation of the hit because it will hit different shapes and they need there own calculation. it is here so that You MUST implement hit() in the specified shape and the child decide how
    public abstract Optional<HitRecord> hit(Ray ray, double tMin, double tMax);

    // Shared helper: make sure the surface normal points toward the incoming ray rather than in the same direction as it.
    //A normal is a vector pointing straight out from a surface.
    //the result of this method to check if the vectors point in the same or opposite direction.
    // normal -> ->rayDir (positive)... normal <- ->rayDir (negative) normal is pointing the wrong way relative to the incoming ray. So we flip it.
    protected static Vector3D faceForward(Vector3D normal, Vector3D rayDir) {
        return normal.dot(rayDir) > 0 ? normal.negate() : normal;
    }
}

