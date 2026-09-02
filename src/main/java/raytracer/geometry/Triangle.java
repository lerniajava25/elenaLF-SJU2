package raytracer.geometry;

import raytracer.material.Material;
import raytracer.math.Ray;
import raytracer.math.Vector3D;

import java.util.Optional;

// resource for the triangle calculation [https://en.wikipedia.org/wiki/M%C3%B6ller%E2%80%93Trumbore_intersection_algorithm]
// https://www.lighthouse3d.com/tutorials/maths/ray-triangle-intersection/
public final class Triangle extends Shape {

    //1e-8 = 0.00000001 a very small number i need it because computers aren't perfect when working with decimal numbers.
    private static final double EPSILON = 1e-8;

 // 3 points in the triangle for each corner(point)
    private final Vector3D v0, v1, v2;

    public Triangle(Vector3D v0, Vector3D v1, Vector3D v2, Material material) {
        super(material);
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
        Vector3D edge1 = v1.subtract(v0);
        Vector3D edge2 = v2.subtract(v0);

        Vector3D pvec = ray.direction().cross(edge2);
        double det = edge1.dot(pvec);
        if (det > -EPSILON && det < EPSILON) return Optional.empty(); // parallel
        double invDet = 1.0 / det;

        Vector3D tvec = ray.origin().subtract(v0);
        double u = tvec.dot(pvec) * invDet;
        if (u < 0 || u > 1) return Optional.empty();

        Vector3D qvec = tvec.cross(edge1);
        double v = ray.direction().dot(qvec) * invDet;
        if (v < 0 || u + v > 1) return Optional.empty();

        double t = edge2.dot(qvec) * invDet;
        if (t < tMin || t > tMax) return Optional.empty();

        Vector3D point = ray.pointAt(t);
        Vector3D normal = faceForward(edge1.cross(edge2).normalize(), ray.direction());
        return Optional.of(new HitRecord(t, point, normal, material()));
    }
}

