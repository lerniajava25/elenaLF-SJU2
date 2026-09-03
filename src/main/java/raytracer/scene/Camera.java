package raytracer.scene;

import raytracer.math.Ray;
import raytracer.math.Vector3D;

/**
 * A configurable pinhole camera: sits at lookFrom, aims at lookAt, with a vertical field of view. It builds a viewport rectangle in front of the eye;
 * getRay maps screen coordinates (s, t) in [0,1] to a ray through that viewport, which is what lets the renderer jitter rays for anti-aliasing.
 */
public final class Camera {
    // The camera creates rays that travel from the camera through an imaginary screen into the 3D world maybe a window(a viewport)? .
    private final Vector3D origin; //camera position
    private final Vector3D lowerLeft;
    private final Vector3D horizontal; // screen's width and right direction
    private final Vector3D vertical; //screen's height and up direction

    // lookFrom: camera position, lookAt: what point should the camera look toward, up: Put the camera here and point it at the sphere. so it keeps the top of the camera pointing roughly toward positive Y
    public Camera(Vector3D lookFrom, Vector3D lookAt, Vector3D up,
                  double verticalFovDeg, double aspectRatio) {

        // Calculate how tall the screen must be to produce this field of view.
        double halfHeight = Math.tan(Math.toRadians(verticalFovDeg) / 2.0); //verticalFovDeg: field of view (how narrow or wide) this is the field view expecting a radia
        double viewportHeight = 2.0 * halfHeight;
        double viewportWidth = aspectRatio * viewportHeight; // aspectRatio: width/height

        //look from camera , look at target
        Vector3D w = lookFrom.subtract(lookAt).normalize();  // camera points backward, .normalize() we look on the direction not not how far a part lookFrom and lookAt
        Vector3D u = up.cross(w).normalize();                // camera points right
        Vector3D v = w.cross(u);                             // points up

        this.origin = lookFrom;
        this.horizontal = u.scale(viewportWidth);
        this.vertical = v.scale(viewportHeight);
        this.lowerLeft = origin
                .subtract(horizontal.scale(0.5))
                .subtract(vertical.scale(0.5))
                .subtract(w);
    }

    //left..1=right, t: 0=bottom..1=top.
    public Ray getRay(double s, double t) {
        Vector3D target = lowerLeft.add(horizontal.scale(s)).add(vertical.scale(t));
        return new Ray(origin, target.subtract(origin).normalize());
    }
}
