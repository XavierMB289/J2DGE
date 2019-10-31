package generate;

/**
 * 
 * @author Xavier Bennett
 * 
 * @desc Shamelessly stolen from the P5.js library. All credit goes to them for making it, and me for transcribing it!
 *
 */

public class PerlinNoise {
	
	int PERLIN_YWRAPB = 4;
	int PERLIN_YWRAP = 1 << PERLIN_YWRAPB;
	int PERLIN_ZWRAPB = 8;
	int PERLIN_ZWRAP = 1 << PERLIN_ZWRAPB;
	int PERLIN_SIZE = 4095;
	
	int perlin_octaves = 4;
	double perlin_amp_falloff = 0.5;
	
	double[] perlin = null;
	
	private double scaled_cosine(double i) {
		return 0.5 * (1.0 - Math.cos(i * Math.PI));
	}
	
	public double noise(int x, int y) {
		if(perlin == null) {
			perlin = new double[PERLIN_SIZE + 1];
			for(int i = 0; i < PERLIN_SIZE+1; i++) {
				perlin[i] = Math.random();
			}
		}
		if(x < 0) {
			x = -x;
		}
		if(y < 0) {
			y = -y;
		}
		int xi = (int)Math.floor(x),
				yi = (int)Math.floor(y),
				zi = (int)Math.floor(0);
		double xf = x - xi;
		double yf = y - yi;
		double zf = 0 - zi;
		double rxf, ryf;
		
		double r = 0;
		double ampl = 0.5;
		
		double n1, n2, n3;
		
		for(int o = 0; o < perlin_octaves; o++) {
			int of = xi + (yi << PERLIN_YWRAPB) + (zi << PERLIN_ZWRAPB);
			
			rxf = scaled_cosine(xf);
			ryf = scaled_cosine(yf);
			
			n1 = perlin[of & PERLIN_SIZE];
			n1 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n1);
			n2 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
			n2 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n2);
			n1 += ryf * (n2 - n1);
			
			of += PERLIN_ZWRAP;
			n2 = perlin[of & PERLIN_SIZE];
			n2 += rxf * (perlin[(of + 1) & PERLIN_SIZE] - n2);
			n3 = perlin[(of + PERLIN_YWRAP) & PERLIN_SIZE];
			n3 += rxf * (perlin[(of + PERLIN_YWRAP + 1) & PERLIN_SIZE] - n3);
			n2 += ryf * (n3 - n2);
			
			n1 += scaled_cosine(zf) * (n2 - n1);
			
			r += n1 * ampl;
			ampl *= perlin_amp_falloff;
			xi <<= 1;
			xf *= 2;
			yi <<= 1;
			yf *= 2;
			zi <<= 1;
			zf *= 2;
			
			if(xf >= 1.0) {
				xi++;
				xf--;
			}
			if(yf >= 1.0) {
				yi++;
				yf--;
			}
			if(zf >= 1.0) {
				zi++;
				zf--;
			}
		}
		return r;
	}
	
	public void noiseDetail(int oct, double falloff) {
		if(oct > 0) {
			perlin_octaves = oct;
		}
		if(falloff > 0) {
			perlin_amp_falloff = falloff;
		}
	}
	
	public void noiseSeed(int seed) {
		LCG lcg = new LCG();
		lcg.setSeed(seed);
		perlin = new double[PERLIN_SIZE + 1];
		for(int i = 0; i < PERLIN_SIZE+1; i++) {
			perlin[i] = lcg.rand();
		}
	}

}

class LCG{
	long m = 4294967296L;
	long a = 1664525;
	long c = 1013904223;
	
	double seed, z;
	
	public void setSeed(double val) {
		z = (int)(Double.isNaN(val) ? Math.random() * m : val) >> 0;
	}
	
	public double getSeed() {
		return seed;
	}
	
	public double rand() {
		z = (a * z + c) % m;
		return z / m;
	}
}
