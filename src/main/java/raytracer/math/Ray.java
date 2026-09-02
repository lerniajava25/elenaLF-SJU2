package raytracer.math;

//   A ray is: origin (starting position of the ray) + t (How far is the ray traveling/distance) * direction. Direction(Which way does it travel?) is kept unit length, so t is the real distance travelled along the ray.
public record Ray(Vector3D origin, Vector3D direction) {
    public Vector3D pointAt(double t){
        return origin.add(direction.scale(t));
    }
}
