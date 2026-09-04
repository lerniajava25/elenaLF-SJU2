package raytracer;

import raytracer.geometry.Sphere;
import raytracer.geometry.Triangle;
import raytracer.material.Lambertian;
import raytracer.material.SolidColor;
import raytracer.math.Color;
import raytracer.math.Vector3D;
import raytracer.render.Image;
import raytracer.render.Renderer;
import raytracer.scene.Camera;
import raytracer.scene.PointLight;
import raytracer.scene.Scene;

import java.io.IOException;
import java.nio.file.Path;

public final class Main {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 338;                 // ~16:9
    private static final int SAMPLES_PER_PIXEL = 64;       // anti-aliasing

    public static void main(String[] args) throws IOException {
        String base = args.length > 0 ? args[0] : "render";

        // Materials
        Lambertian pink = new Lambertian(new Color(0.75, 0.52, 0.90));
        Lambertian orange = new Lambertian(new Color(0.95, 0.55, 0.15));
        Lambertian blue   = new Lambertian(new Color(0.20, 0.40, 0.85));
        Lambertian green  = new Lambertian(new Color(0.30, 0.70, 0.35));
        SolidColor flatRed = new SolidColor(new Color(0.85, 0.20, 0.20));

        Scene scene = new Scene()
                .add(new Sphere(new Vector3D(0.0, 0.0, -1.2), 0.5, orange))
                .add(new Sphere(new Vector3D(-1.1, 0.0, -1.5), 0.5, blue))
                .add(new Sphere(new Vector3D(1.1, 0.0, -1.5), 0.5, flatRed))   // unlit
                .add(new Triangle(
                        new Vector3D(-0.4, 0.6, -3.3), // bottom-left
                        new Vector3D(0.4, 0.6, -3.3),// bottom-right
                        new Vector3D(0.0, 1.3, -3.3), green)) // top
                .add(new Triangle(
                        new Vector3D(1.8, -0.4, -1.5),
                        new Vector3D(2.5, -0.4, -1.5),
                        new Vector3D(2.0, -0.3, -1.5),pink))
                .addLight(new PointLight(new Vector3D(3, 4, 2), Color.WHITE, 1.1))
                .addLight(new PointLight(new Vector3D(-3, 2, 2),
                        new Color(0.5, 0.6, 1.0), 0.3));


        Camera camera = new Camera(
                new Vector3D(0, 0.6, 1.8),
                new Vector3D(0, 0.0, -1.0),
                new Vector3D(0, 1, 0),
                50.0,
                (double) WIDTH / HEIGHT);

        long start = System.currentTimeMillis();
        Image image = new Renderer(SAMPLES_PER_PIXEL).render(scene, camera, WIDTH, HEIGHT);
        long elapsed = System.currentTimeMillis() - start;

        image.savePng(Path.of(base + ".png"));
        image.savePpm(Path.of(base + ".ppm"));

        System.out.printf("Rendered %dx%d at %d spp in %d ms -> %s.png, %s.ppm%n",
                WIDTH, HEIGHT, SAMPLES_PER_PIXEL, elapsed, base, base);
    }
}

//I want a 600 × 338 image.
//
//Use 64 rays per pixel.
//
// Create:
// - pink material
//- orange material
//- blue material
//- green material
//- flat red material
//
//Create a world.
//Put:
// - an orange sphere in the middle
//- a blue sphere on the left
//- a red sphere on the right
//- a green triangle above
//- a pink triangle on the right
//
//Add two lights.
//
//Put the camera here.
//Aim it there.
//
//Start timer.
//
//Tell Renderer:
//        "Render this world from this camera."
//
//Get the finished Image back.
//
//Stop timer.
//
//Save image as PNG.
//Save image as PPM.
//
//Print how long it took.