package raytracer.scene;

import raytracer.geometry.HitRecord;
import raytracer.geometry.Shape;
import raytracer.math.Color;
import raytracer.math.Ray;
import raytracer.math.Vector3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

 // The world: a list of shapes and lights plus a background and ambient colour.Shapes are stored as a List<Shape> and looped over when finding the nearest hit. The class also answers shadow queries, which is all a material needs to cast shadows. Setters return this for a fluent build.
//this class is for everything that exists in my 3D world, and a helper for answering: what does this ray hit!!!
// i need to store in this class: 1/ objects (spheres, triangles) 2-all lights 3- the background color 4- the anbient light 5- logic for finding the closest object hit by a ray 6- logic for checking whether something is in shadow

public final class Scene {

    private static final double SHADOW_EPSILON = 1e-4; //Ignore anything extremely close to the starting position.

    private final List<Shape> shapes = new ArrayList<>(); // (polymorphism)store all shapes in one  shape list/ shall I use Optional?
    private final List<PointLight> lights = new ArrayList<>();
    private Color background = new Color(0.50, 0.70, 1.0);
    private Color ambient = new Color(0.08, 0.08, 0.10);
    //Ambient light is a small amount of light that exists everywhere.
    //Without ambient light, something in shadow might become completely black.

    //methods
    public Scene add(Shape shape)         { shapes.add(shape); return this; }
    public Scene addLight(PointLight l)   { lights.add(l);     return this; }
    public Scene background(Color c)      { this.background = c; return this; }
    public Scene ambient(Color c)         { this.ambient = c;   return this; } //surrounding lights

    // getter methods:
    public List<PointLight> lights() { return lights; }
    public Color background()        { return background; }
    public Color ambient()           { return ambient; }

    //Send this ray through every shape in the scene and return the nearest collision.
    public Optional<HitRecord> hit(Ray ray, double tMin, double tMax) {
        Optional<HitRecord> closest = Optional.empty(); //no hit/ nothing
        double closestT = tMax; //start with maximum allowed distance
        for (Shape shape : shapes) {
            Optional<HitRecord> candidate = shape.hit(ray, tMin, closestT);
            if (candidate.isPresent()) { //Did this shape actually get hit?
                closest = candidate; //store the hit
                closestT = candidate.get().t();
            }
        }
        return closest;
    }

    // shadow detection: Is anything blocking the straight path  between position from - position to
    //blocked from view/light?
    public boolean isOccluded(Vector3D from, Vector3D to) {
        Vector3D toTarget = to.subtract(from);
        double distance = toTarget.length();
        Ray shadowRay = new Ray(from, toTarget.scale(1.0 / distance));
        return hit(shadowRay, SHADOW_EPSILON, distance - SHADOW_EPSILON).isPresent();
    }
}
//SCENE
//
//store:
//shapes
//lights
//background color
//ambient light
//
//ADD SHAPE:
//put shape into shapes list
//
//ADD LIGHT:
//put light into lights list
//
//HIT(ray):
//closest hit = nothing
//closest distance = maximum allowed distance
//
//FOR every shape:
//ask shape: does this ray hit you before closest distance?
//
//IF yes:
//remember this hit
//update closest distance
//RETURN closest hit
//
//
//IS OCCLUDED(surface, light):
//calculate direction to light
//calculate distance to light
//create ray toward light
//IF ray hits something before reaching light:
// return true
//
//ELSE:return false