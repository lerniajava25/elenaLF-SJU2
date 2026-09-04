package raytracer.render;

import raytracer.geometry.HitRecord;
import raytracer.math.Color;
import raytracer.math.Ray;
import raytracer.scene.Camera;
import raytracer.scene.Scene;

import java.util.Optional;
import java.util.Random;


// Renders a Scene through a Camera into an Image.
//The renderer goes through every pixel, shoots rays from the camera, checks what each ray hits, asks the hit object's material what color it should be, and stores that color in the image.

public  class Renderer {

    private static final double MIN_DISTANCE = 1e-4; // 0.0001 instead of 0.

    private final int samplesPerPixel; //how many rays per pixel and the colors are averaged togother . This controls anti-aliasing by to make the edges of the shape looks sm0oth, if I shoot 1 ray per 1 pixel the edges will look sharp , it will look like stairs
    private final Random random = new Random(42); // to get the seed to use a fixed number everytime i run the program: because random is not actually random this is an algorithm I  need a random number to slightly move each ray inside the pixel (jitter)

    public Renderer(int samplesPerPixel) {
        this.samplesPerPixel = Math.max(1, samplesPerPixel);
    }

    public Image render(Scene scene, Camera camera, int width, int height) {
        Image image = new Image(width, height); //image with no pixels

            //calculate the color for each pixel
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color sum = Color.BLACK;// start at color black and accumulate all color for this pixel rgb (0,0,0)
                for (int s = 0; s < samplesPerPixel; s++) { //to chose a position inside a pixel and shoot thwe ray
                    double jitterX = samplesPerPixel == 1 ? 0.5 : random.nextDouble(); // number (0-1), the middle of an pixel is (0.5,0.5)
                    double jitterY = samplesPerPixel == 1 ? 0.5 : random.nextDouble();
                    //convert pixel X to camera coordinates i need a number between (0-1)
                    double u = (x + jitterX) / (width - 1); // horizontal position
                    double v = (height - 1 - y + jitterY) / (height - 1); //vertical position// flip Y

                    sum = sum.add(traceRay(scene, camera.getRay(u, v)));
                }
                image.setPixel(x, y, sum.scale(1.0 / samplesPerPixel).clamp());
            }
        }
        return image;
    }

    private Color traceRay(Scene scene, Ray ray) {
        Optional<HitRecord> hit = scene.hit(ray, MIN_DISTANCE, Double.POSITIVE_INFINITY);
        if (hit.isEmpty()) {
            return scene.background();
        }
        HitRecord h = hit.get();
        return h.material().shade(h.point(), h.normal(), scene);
    }
}
//CREATE an empty image
//
//FOR every row:
//
//FOR every pixel in that row:
//
//start total color as black
//
//FOR every sample:
//
//choose a position inside the pixel
//
//convert the pixel position to camera coordinates
//
//ask camera to create a ray
//
//shoot the ray into the scene
//
//IF ray hits nothing:
//use background color
//
//ELSE:
//get the material of the object
//ask material to calculate its color
//
//add this color to total
//
//calculate average color
//
//save average color into pixel
//
//RETURN finished image
