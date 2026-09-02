package raytracer.geometry;

import raytracer.material.Material;
import raytracer.math.Vector3D;

// the result of a ray hitting a surface It needs the distance , the point, how it face the ray

public record HitRecord(double t , Vector3D point, Vector3D normal, Material material) {
}
