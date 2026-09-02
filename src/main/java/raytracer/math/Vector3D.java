package raytracer.math;

// vector is x, y, z so we will create a point and a direction in the space/scene, it the position of the a dot moving in the space from the center point, the vector is the arrow between point 0,0,0 to eg, 3,2,5, so we have a position vector and a direction/movement vector
// think in gaming when a player move. left, right, up, down , how far?
 // so here in my math I want to add/sbstrarct, scale and negate to recalculate the vector..
// this is important for camera position, light direction , object position, surface

public record Vector3D(double x, double y, double z){
    //Calculate the dot in hand between this vector and the other vector (o).
    //to move something
 public Vector3D add(Vector3D o){
     return new Vector3D(x+o.x, y+o.y, z+o.z);
 }

 //gives the direction between 2 points like the camera/ light  to the object
    public Vector3D substract(Vector3D o){
     return new Vector3D(x-o.x, y-o.y, z-o.z);
 }
   // if i scale by 2 I am making the vector/distance as twice as long.
    //distance
public Vector3D scale(double s){
     return new Vector3D(x*s, y*s, z*s);
    }

    //turn around/ change to the opposite direction/ go right or left
    public Vector3D negate() { return new Vector3D(-x, -y, -z); }

    // how much are two directions facing each other. for the lighting
    public double dot(Vector3D o) { return x * o.x + y * o.y + z * o.z; }


    public Vector3D cross(Vector3D o) {
        return new Vector3D(
                y * o.z - z * o.y,
                z * o.x - x * o.z,
                x * o.y - y * o.x);
    }

    // How far apart are two points?
    //Vector3D camera = new Vector3D(0, 0, 0);
    //Vector3D object = new Vector3D(3, 4, 0);
    //Vector3D difference = object.subtract(camera);
    //double distance = difference.length();
    public double length() { return Math.sqrt(dot(this)); }

    // which way does it point:
    public Vector3D normalize() { return scale(1.0 / length()); }
}
