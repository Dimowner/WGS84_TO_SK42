package ua.app.dronnen.mapapplication;

/**
 * Класс для перевода географических координат в системах WGS84 и СК42.
 * Код скопирован с ресурса www.gis-lab.info
 * http://gis-lab.info/qa/wgs84-sk42-wgs84-formula.html
 * http://gis-lab.info/qa/datum-transform-methods.html
 * @author Raven
 */
public class WGS84_SK42_Translator {

	/**
	 * Пересчет широты из WGS-84 в СК-42.
	 * @param Bd широта
	 * @param Ld долгота
	 * @param H высота
	 * @return  широта в Ск-42
	 */
	public static double WGS84_SK42_Lat(double Bd, double Ld, double H) {
		return Bd - dB(Bd, Ld, H) / 3600;
	}

	/**
	 * Пересчет широты из СК-42 в WGS-84.
	 * @param Bd широта
	 * @param Ld долгота
	 * @param H высота
	 * @return широта в WGS-84
	 */
	public static double SK42_WGS84_Lat(double Bd, double Ld, double H) {
		return Bd + dB(Bd, Ld, H) / 3600;
	}

	/**
	 * Пересчет долготы из WGS-84 в СК-42.
	 * @param Bd широта
	 * @param Ld долгота
	 * @param H высота
	 * @return долгота в СК-42
	 */
	public static double WGS84_SK42_Long(double Bd, double Ld, double H) {
		return Ld - dL(Bd, Ld, H) / 3600;
	}

	/**
	 * Пересчет долготы из СК-42 в WGS-84.
	 * @param Bd широта
	 * @param Ld долгота
	 * @param H высота
	 * @return долгота в WGS-84
	 */
	public static double SK42_WGS84_Long(double Bd, double Ld, double H) {
		return Ld + dL(Bd, Ld, H) / 3600;
	}

	/**
	 *
	 * @param Bd широта
	 * @param Ld долгота
	 * @param H высота
	 * @return
	 */
	public static double dB(double Bd, double Ld, double H) {
		double B = Bd * Math.PI / 180;
		double L = Ld * Math.PI / 180;
		double M = a * (1 - e2) / Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), 1.5);
		double N = a * Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), -0.5);
		double result = ro / (M + H) * (N / a * e2 * Math.sin(B) * Math.cos(B) * da
				+ (Math.pow(N, 2) / Math.pow(a, 2) + 1) * N * Math.sin(B) *
				Math.cos(B) * de2 / 2 - (dx * Math.cos(L) + dy * Math.sin(L)) *
				Math.sin(B) + dz * Math.cos(B)) - wx * Math.sin(L) * (1 + e2 *
				Math.cos(2 * B)) + wy * Math.cos(L) * (1 + e2 * Math.cos(2 * B)) -
				ro * ms * e2 * Math.sin(B) * Math.cos(B);
		return result;
	}

	/**
	 *
	 * @param Bd широта
	 * @param Ld долгота
	 * @param H высота
	 * @return
	 */
	public static double dL (double Bd, double Ld, double H) {
		double B = Bd * Math.PI / 180;
		double L = Ld * Math.PI / 180;
		double N = a * Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), -0.5);
		return ro / ((N + H) * Math.cos(B)) * (-dx * Math.sin(L) + dy * Math.cos(L))
				+ Math.tan(B) * (1 - e2) * (wx * Math.cos(L) + wy * Math.sin(L)) - wz;
	}

	/**
	 *
	 * @param Bd широта
	 * @param Ld долгота
	 * @param H высота
	 * @return
	 */
	public static double WGS84Alt(double Bd, double Ld, double H) {
		double B = Bd * Math.PI / 180;
		double L = Ld * Math.PI / 180;
		double N = a * Math.pow((1 - e2 * Math.pow(Math.sin(B), 2)), -0.5);
		double dH = -a / N * da + N * Math.pow(Math.sin(B), 2) * de2 / 2 +
				(dx * Math.cos(L) + dy * Math.sin(L)) *
						Math.cos(B) + dz * Math.sin(B) - N * e2 *
				Math.sin(B) * Math.cos(B) *
				(wx / ro * Math.sin(L) - wy / ro * Math.cos(L)) +
				(Math.pow(a, 2) / N + H) * ms;
		return H + dH;
	}

	// Математические константы
	private static final double ro = 206264.8062;          // Число угловых секунд в радиане
	// Эллипсоид Красовского
	private static final double aP = 6378245;              // Большая полуось
	private static final double  alP = 1 / 298.3;          // Сжатие
	private static final double  e2P = 2 * alP - Math.pow(alP, 2);  // Квадрат эксцентриситета
	// Эллипсоид WGS84 (GRS80, эти два эллипсоида сходны по большинству параметров)
	private static final double aW = 6378137;                  // Большая полуось
	private static final double alW = 1 / 298.257223563;       // Сжатие
	private static final double e2W = 2 * alW - Math.pow(alW, 2);// Квадрат эксцентриситета
	// Вспомогательные значения для преобразования эллипсоидов
	private static final double  a   = (aP + aW) / 2;
	private static final double  e2  = (e2P + e2W) / 2;
	private static final double  da  = aW - aP;
	private static final double  de2 = e2W - e2P;
	// Линейные элементы трансформирования, в метрах
	private static final double dx = 23.92;
	private static final double dy = -141.27;
	private static final double dz = -80.9;
	// Угловые элементы трансформирования, в секундах
	private static final double wx = 0;
	private static final double wy = 0;
	private static final double wz = 0;
	// Дифференциальное различие масштабов
	private static final double ms = 0;
}

// <editor-fold defaultstate="collapsed" desc="Код на Visual Basic">
//Const Pi As Double = 3.14159265358979 ' Число Пи
//Const ro As Double = 206264.8062 ' Число угловых секунд в радиане
//
//' Эллипсоид Красовского
//Const aP As Double = 6378245 ' Большая полуось
//Const alP As Double = 1 / 298.3 ' Сжатие
//Const e2P As Double = 2 * alP - alP ^ 2 ' Квадрат эксцентриситета
//
//'Эллипсоид WGS84 (GRS80, эти два эллипсоида сходны по большинству параметров)
//Const aW As Double = 6378137 ' Большая полуось
//Const alW As Double = 1 / 298.257223563 ' Сжатие
//Const e2W As Double = 2 * alW - alW ^ 2 ' Квадрат эксцентриситета
//
//' Вспомогательные значения для преобразования эллипсоидов
//Const a As Double = (aP + aW) / 2
//Const e2 As Double = (e2P + e2W) / 2
//Const da As Double = aW - aP
//Const de2 As Double = e2W - e2P
//
//' Линейные элементы трансформирования, в метрах
//Const dx As Double = 23.92
//Const dy As Double = -141.27
//Const dz As Double = -80.9
//
//' Угловые элементы трансформирования, в секундах
//Const wx As Double = 0
//Const wy As Double = 0
//Const wz As Double = 0
//
//' Дифференциальное различие масштабов
//Const ms As Double = 0
//
//Function WGS84_SK42_Lat(Bd, Ld, H) As Double
//    WGS84_SK42_Lat = Bd - dB(Bd, Ld, H) / 3600
//End Function
//
//Function SK42_WGS84_Lat(Bd, Ld, H) As Double
//    SK42_WGS84_Lat = Bd + dB(Bd, Ld, H) / 3600
//End Function
//
//Function WGS84_SK42_Long(Bd, Ld, H) As Double
//    WGS84_SK42_Long = Ld - dL(Bd, Ld, H) / 3600
//End Function
//
//Function SK42_WGS84_Long(Bd, Ld, H) As Double
//    SK42_WGS84_Long = Ld + dL(Bd, Ld, H) / 3600
//End Function
//
//Function dB(Bd, Ld, H) As Double
//    Dim B, L, M, N As Double
//    B = Bd * Pi / 180
//    L = Ld * Pi / 180
//    M = a * (1 - e2) / (1 - e2 * Sin(B) ^ 2) ^ 1.5
//    N = a * (1 - e2 * Sin(B) ^ 2) ^ -0.5
//    dB = ro / (M + H) * (N / a * e2 * Sin(B) * Cos(B) * da _
//     + (N ^ 2 / a ^ 2 + 1) * N * Sin(B) * Cos(B) * de2 / 2 _
//     - (dx * Cos(L) + dy * Sin(L)) * Sin(B) + dz * Cos(B)) _
//     - wx * Sin(L) * (1 + e2 * Cos(2 * B)) _
//     + wy * Cos(L) * (1 + e2 * Cos(2 * B)) _
//     - ro * ms * e2 * Sin(B) * Cos(B)
//End Function
//
//Function dL(Bd, Ld, H) As Double
//    Dim B, L, N As Double
//    B = Bd * Pi / 180
//    L = Ld * Pi / 180
//    N = a * (1 - e2 * Sin(B) ^ 2) ^ -0.5
//    dL = ro / ((N + H) * Cos(B)) * (-dx * Sin(L) + dy * Cos(L)) _
//      + Tan(B) * (1 - e2) * (wx * Cos(L) + wy * Sin(L)) - wz
//End Function
//
//Function WGS84Alt(Bd, Ld, H) As Double
//    Dim B, L, N, dH As Double
//    B = Bd * Pi / 180
//    L = Ld * Pi / 180
//    N = a * (1 - e2 * Sin(B) ^ 2) ^ -0.5
//    dH = -a / N * da + N * Sin(B) ^ 2 * de2 / 2 _
//      + (dx * Cos(L) + dy * Sin(L)) * Cos(B) + dz * Sin(B) _
//      - N * e2 * Sin(B) * Cos(B) * (wx / ro * Sin(L) - wy / ro * Cos(L)) _
//      + (a ^ 2 / N + H) * ms
//    WGS84Alt = H + dH
//End Function
//</editor-fold>
