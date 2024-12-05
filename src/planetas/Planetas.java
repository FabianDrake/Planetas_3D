package planetas;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;


public class Planetas {

    public static void main(String[] args) {
        new Planetas();
    }

    public Planetas() {
        // Crear el universo 3D
        SimpleUniverse universo = new SimpleUniverse();

        // Centrar la vista y ampliar la ventana
        universo.getViewingPlatform().setNominalViewingTransform();

        // Ajustar el tamaño de la ventana
        Canvas3D canvas = universo.getCanvas();
        Frame frame = new Frame("Planetas - 22110092");
        frame.setSize(800, 800);  // Tamaño de la ventana
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.setVisible(true);

        // Centrar la ventana en la pantalla
        frame.setLocationRelativeTo(null);

        BranchGroup grupoRaiz = crearEscena();
        universo.addBranchGraph(grupoRaiz);
    }

    private BranchGroup crearEscena() {
        BranchGroup grupoRaiz = new BranchGroup();

        // Fondo (Imagen del universo)
        TextureLoader texturaFondo = new TextureLoader("C:\\Users\\arnol\\Documents\\3DJAVA\\src\\pkg3djava\\galaxia.jpg", null);
        Background fondo = new Background(texturaFondo.getImage());
        fondo.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        grupoRaiz.addChild(fondo);

        // Luz puntual simulando el Sol
        PointLight luzPuntual = new PointLight(
                new Color3f(1.0f, 1.0f, 1.0f),
                new Point3f(0.0f, 0.0f, 0.0f),
                new Point3f(1.0f, 0.0f, 0.0f)
        );
        luzPuntual.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        grupoRaiz.addChild(luzPuntual);

        // Crear el primer planeta (Júpiter)
        Appearance aparienciaPlaneta1 = crearApariencia("C:\\Users\\arnol\\Documents\\3DJAVA\\src\\pkg3djava\\jupiter.jpg");
        Sphere planeta1 = new Sphere(0.2f, Sphere.GENERATE_TEXTURE_COORDS | Sphere.GENERATE_NORMALS, aparienciaPlaneta1);
        TransformGroup tgPlaneta1 = crearPlanetaConOrbita(planeta1, 0.6f, 10000);
        grupoRaiz.addChild(tgPlaneta1);

        // Crear el segundo planeta (Luna)
        Appearance aparienciaPlaneta2 = crearApariencia("C:\\Users\\arnol\\Documents\\3DJAVA\\src\\pkg3djava\\luna.jpg");
        Sphere planeta2 = new Sphere(0.1f, Sphere.GENERATE_TEXTURE_COORDS | Sphere.GENERATE_NORMALS, aparienciaPlaneta2);
        TransformGroup tgPlaneta2 = crearPlanetaConOrbita(planeta2, 0.3f, 15000);
        grupoRaiz.addChild(tgPlaneta2);

        // Texto 3D flotante en la parte superior
        Text3D texto3D = new Text3D(
                new Font3D(new Font("SansSerif", Font.BOLD, 1), new FontExtrusion()),
                "Fabian"
        );
        texto3D.setAlignment(Text3D.ALIGN_CENTER);  // Alineación centrada
        Shape3D formaTexto = new Shape3D(texto3D);

        TransformGroup tgTexto = new TransformGroup();
        Transform3D transformTexto = new Transform3D();
        transformTexto.setScale(0.5);  // Ajuste de tamaño
        transformTexto.setTranslation(new Vector3d(0.0, 0.8, -3.0));  // Posicionando el texto en la parte superior
        tgTexto.setTransform(transformTexto);

        tgTexto.addChild(formaTexto);
        grupoRaiz.addChild(tgTexto);

        return grupoRaiz;
    }

    private TransformGroup crearPlanetaConOrbita(Sphere planeta, float radioOrbita, int duracionOrbita) {
        TransformGroup tgOrbita = new TransformGroup();
        tgOrbita.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D translacionOrbita = new Transform3D();
        translacionOrbita.setTranslation(new Vector3d(radioOrbita, 0.0, 0.0));
        TransformGroup tgTraslacion = new TransformGroup(translacionOrbita);

        TransformGroup tgRotacionPlaneta = new TransformGroup();
        tgRotacionPlaneta.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        tgRotacionPlaneta.addChild(planeta);
        tgTraslacion.addChild(tgRotacionPlaneta);
        tgOrbita.addChild(tgTraslacion);

        Alpha alphaOrbita = new Alpha(-1, duracionOrbita);
        RotationInterpolator rotacionOrbita = new RotationInterpolator(alphaOrbita, tgOrbita);
        rotacionOrbita.setTransformAxis(new Transform3D());
        rotacionOrbita.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        tgOrbita.addChild(rotacionOrbita);

        Alpha alphaRotacion = new Alpha(-1, 4000);
        RotationInterpolator rotacionPlaneta = new RotationInterpolator(alphaRotacion, tgRotacionPlaneta);
        rotacionPlaneta.setTransformAxis(new Transform3D());
        rotacionPlaneta.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0));
        tgRotacionPlaneta.addChild(rotacionPlaneta);

        return tgOrbita;
    }

    private Appearance crearApariencia(String archivoTextura) {
        TextureLoader cargadorTextura = new TextureLoader(archivoTextura, "LUMINANCE", new Container());
        Texture textura = cargadorTextura.getTexture();

        Appearance apariencia = new Appearance();
        apariencia.setTexture(textura);
        return apariencia;
    }
}

