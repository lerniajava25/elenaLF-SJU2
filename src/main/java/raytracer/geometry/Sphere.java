package raytracer.geometry;

import raytracer.material.Material;
import raytracer.math.Ray;
import raytracer.math.Vector3D;

import java.util.Optional;

    // A sphere. Intersection comes from substituting the ray into (P - center)² = radius², which gives a quadratic in t.
     //  Does this ray hit this sphere? If yes, where does it hit, and what is the surface direction at that point?
    public final class Sphere extends Shape {

        private final Vector3D center;
        private final double radius;

        public Sphere(Vector3D center, double radius, Material material) {
            super(material);
            this.center = center;
            this.radius = radius;
        }

        // oc (origin - center) is: vector from the sphere's center to the ray origin.
        @Override
        public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
            Vector3D oc = ray.origin().subtract(center);
            double a = ray.direction().dot(ray.direction());
            double halfB = oc.dot(ray.direction());
            double c = oc.dot(oc) - radius * radius;

            //The discriminant tells us whether the ray intersects the sphere. a miss=0 or possitive: the ray passes the saphere
            double discriminant = halfB * halfB - a * c;
            if (discriminant < 0) return Optional.empty();
            double sqrtD = Math.sqrt(discriminant);

            double t = (-halfB - sqrtD) / a;                 // nearer root/first intersection
                // check if this intersection inside the distance range that we're interested in. t=0 is the camera's position
               //tMin is also useful for avoiding tiny floating point self intersections around t = 0 so the best is to start with tMin=0.001
            if (t < tMin || t > tMax) {
                t = (-halfB + sqrtD) / a;                    // farther root /intersection


                if (t < tMin || t > tMax) return Optional.empty();
            }

            Vector3D point = ray.pointAt(t);
            Vector3D normal = faceForward(point.subtract(center).scale(1.0 / radius),
                    ray.direction());
            return Optional.of(new HitRecord(t, point, normal, material())); // material here is a method from the shape class. I made the variable private there
        }
    }

    //steps/ pseudocode:
// 1. Where does the ray start relative to the sphere?
//
// 2. Build the mathematical equation describing ray + sphere.
//
//3. Calculate the discriminant.
//
//4. discriminant < 0?
//            → Ray misses sphere.
//
//5. Otherwise calculate the nearest intersection.
//
//6. Is that intersection outside tMin/tMax? → Try the farther intersection.
//
// 7. Both invalid → No hit.
//
// 8. valid! We have a valid t.
//
//9. Calculate the exact 3D hit point.
//
// 10. Calculate the surface normal.
//
// 11. Make sure normal faces correctly.
//
//12. Return: t,  point, normal and material


