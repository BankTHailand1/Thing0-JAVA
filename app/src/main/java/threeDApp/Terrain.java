package threeDApp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import org.json.JSONObject;

public class Terrain {
    public class TerrainConfig {
        public int seed, size;
        public float scale, heightMultiplier, persistance, lacunarity;
        public int octaves;
    
        public TerrainConfig(String filePath) {
            try {
                String content = new String(Files.readAllBytes(Paths.get(filePath)));
                JSONObject json = new JSONObject(content);
    
                seed = json.getInt("seed");
                scale = json.getFloat("scale");
                heightMultiplier = json.getFloat("heightMultiplier");
                octaves = json.getInt("octaves");
                persistance = json.getFloat("persistance");
                lacunarity = json.getFloat("lacunarity");
                size = json.getInt("size");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public class SimpleTerrainGenerator {
        private Random random;
        private TerrainConfig config;
    
        public SimpleTerrainGenerator(TerrainConfig config) {
            this.config = config;
            this.random = new Random(config.seed);
        }
    
        public float getHeight(int x, int z) {
            // Simple height using random (not as good as Perlin Noise)
            return (random.nextFloat() * 2 - 1) * config.heightMultiplier;
        }
    }
 
/*  public class TerrainGenerator {
        private OpenSimplex2 noise;
        private TerrainConfig config;
    
        public TerrainGenerator(TerrainConfig config) {
            this.config = config;
            this.noise = new OpenSimplex2(config.seed);
        }
    
        public float getHeight(int x, int z) {
            float height = 0;
            float amplitude = 1;
            float frequency = config.scale;
    
            for (int i = 0; i < config.octaves; i++) {
                height += OpenSimplex2.noise2(config.seed, x * frequency, z * frequency) * amplitude;
                amplitude *= config.persistance;
                frequency *= config.lacunarity;
            }
    
            return height * config.heightMultiplier;
        }
    }

    public class TerrainMesh {
        private int vbo, vao;
        private TerrainGenerator generator;
        private TerrainConfig config;

        public TerrainMesh(TerrainGenerator generator, TerrainConfig config) {
            this.generator = generator;
            this.config = config;
            generateMesh();
        }

        private void generateMesh() {
            int size = config.size;
            float[] vertices = new float[size * size * 3];

            int index = 0;
            for (int x = 0; x < size; x++) {
                for (int z = 0; z < size; z++) {
                    float y = generator.getHeight(x, z);
                    vertices[index++] = x;
                    vertices[index++] = y;
                    vertices[index++] = z;
                }
            }

            FloatBuffer vertexBuffer = MemoryUtil.memAllocFloat(vertices.length);
            vertexBuffer.put(vertices).flip();

            vao = GL15.glGenBuffers();
            vbo = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertexBuffer, GL15.GL_STATIC_DRAW);
            MemoryUtil.memFree(vertexBuffer);
        }

        public void render() {
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            glDrawArrays(GL_TRIANGLES, 0, config.size * config.size);
        }
    }*/
}