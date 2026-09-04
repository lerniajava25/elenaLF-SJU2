package raytracer.math;
// combination av RGB (red, green, blue).  0.0: no light (black), 1.0 max light(white)
//the reason i am using 0-1 and not 0-255 is to be able to do the math in an easy way 0 -> 0% , 0.5 ->50 , .75 -> 75% , it will be hard to do this in pixels yet
//0–1 = calculate light and colors
//0–255 = save/display the final 8 bit pixel
public record Color(double r , double g, double b) {

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);

    // usefull if a point of a ball gets lights from 2 lamps so the add gives me the total
    public Color add(Color o){
        return new Color(r+o.r,g+o.g,b+o.b);
    }

    // to calculate when the light become stronger or weaker.
    public Color scale(double s)   { return new Color(r * s, g * s, b * s); }

    // practically used the color of the material (surface) meets the color of the light eg, red ball vs white light
    public Color multiply(Color o) { return new Color(r * o.r, g * o.g, b * o.b); }

    // https://examples.javacodegeeks.com/create-a-clamp-function-in-java/
    // clamp used to keep the colors (r,b,g) within my range min 0 max 1

    public Color clamp(){
        return new Color(
                Math.clamp(r, 0.0,1.0),
                Math.clamp(g, 0.0,1.0),
                Math.clamp(b, 0.0,1.0)
        );
    }
}

