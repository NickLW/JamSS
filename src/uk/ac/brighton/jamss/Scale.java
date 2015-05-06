package uk.ac.brighton.jamss;

/**
 * This class contains and returns arrays for all possible choices
 * of musical scale.
 * @author Nick Walker
 *
 */
public class Scale {

	//MAJOR SCALES

	private static final double[] FFREQUENCIES = { 174.61, 164.81, 146.83, 130.81, 116.54, 110.00, 98.00, 87.31};
	private static final String[] FNAME        = { "F",    "E",    "D",    "C",    "A#",   "A",    "G",   "F"};

	private static final double[] EFREQUENCIES = {164.81, 155.56, 138.59, 123.47, 110.00, 103.83, 92.50, 82.41};
	private static final String[] ENAME        = {"E",    "D#",    "C#",   "B",    "A",    "G#",   "F#",  "E"};

	private static final double[] DsFREQUENCIES = { 155.56, 146.83, 130.81, 116.54, 103.83, 98.00, 87.31, 77.78};
	private static final String[] DsNAME        = { "D#",   "D",    "C",    "A#",   "G#",   "G",   "F",   "D#"};

	private static final double[] DFREQUENCIES = { 146.83, 138.59, 123.47, 110.00, 98.00, 92.50, 82.41, 73.42};
	private static final String[] DNAME        = { "D",    "C#",   "B",    "A",    "G",   "F#",  "E",    "D"};

	private static final double[] CsFREQUENCIES = { 138.59, 130.81, 116.54, 103.83, 92.50, 87.31, 77.78, 69.30};
	private static final String[] CsNAME        = { "C#",   "C",    "A#",   "G#",   "F#",  "F",   "D#",  "C#"};

	private static final double[] CFREQUENCIES = { 130.81, 123.47, 110.00, 98.00, 87.31, 82.41, 73.42, 65.41};
	private static final String[] CNAME        = { "C",    "B",    "A",    "G",   "F",   "E",    "D",    "C"};

	private static final double[] BFREQUENCIES = { 246.94, 233.08, 207.65, 185.00, 164.81, 155.56, 138.59, 123.47};
	private static final String[] BNAME        = { "B",    "A#",   "G#",   "F#",   "E",    "D#",   "C#",   "B"};

	private static final double[] AsFREQUENCIES = { 233.08, 220.00, 196.00, 174.61, 155.56, 146.83, 130.81, 116.54};
	private static final String[] AsNAME        = { "A#",   "A",    "G",    "F",    "D#",   "D",    "C",    "A#"};

	private static final double[] AFREQUENCIES = { 220.00, 207.65, 185.00, 164.81, 146.83, 138.59, 123.47, 110.00};
	private static final String[] ANAME        = { "A",    "G#",   "F#",   "E",    "D",    "C#",   "B",    "A"};

	private static final double[] GsFREQUENCIES = { 207.65, 196.00, 174.61, 155.56, 138.59, 130.81, 116.54, 103.83};
	private static final String[] GsNAME        = { "G#",   "G",    "F",    "D#",   "C#",   "C",    "A#",   "G#"};

	private static final double[] GFREQUENCIES = { 196.00, 185.00, 164.81, 146.83, 130.81, 123.47, 110.00, 98.00};
	private static final String[] GNAME        = { "G",    "F#",   "E",    "D",    "C",    "B",    "A",    "G"};

	private static final double[] FsFREQUENCIES = { 185.00, 174.61, 155.56, 138.59, 123.47, 116.54, 103.83, 92.50};
	private static final String[] FsNAME        = { "F#",   "F",    "D#",   "C#",   "B",    "A#",   "G#",   "F#"};


	//MINOR SCALES

	private static final double[] FFREQUENCIESm = {174.61, 155.56, 138.59, 130.81, 116.54, 103.83, 98.00, 87.31};
	private static final String[] FNAMEm        = {"F",    "D#",    "C#",    "C",    "A#",   "G#",   "G",   "F"};

	private static final double[] EFREQUENCIESm = {164.81, 146.83, 130.81, 123.47, 110.00, 98.00, 92.50, 82.41};
	private static final String[] ENAMEm        = {"E",    "D",    "C",    "B",    "A",    "G",   "F#",   "E"};

	private static final double[] DsFREQUENCIESm = {155.56, 138.59, 123.47, 116.54, 103.83, 92.50, 87.31, 77.78};
	private static final String[] DsNAMEm        = {"D#",   "C#",   "B",    "A#",   "G#",   "F#",  "F",   "D#"};

	private static final double[] DFREQUENCIESm = {146.83, 130.81, 116.54, 110.00, 98.00, 87.31, 82.41, 73.42};
	private static final String[] DNAMEm        = {"D",    "C",    "A#",    "A",    "G",   "F",   "E",  "D"};

	private static final double[] CsFREQUENCIESm = {138.59, 123.47, 110.00, 103.83, 92.50, 82.41, 77.78, 69.30};
	private static final String[] CsNAMEm        = {"C#",    "B",    "A",    "G#",   "F#",   "E", "D#",  "C#"};

	private static final double[] CFREQUENCIESm = {130.81, 116.54, 103.83, 98.00, 87.31, 77.78, 73.42, 65.41};
	private static final String[] CNAMEm        = {"C",    "A#",    "G#",    "G",   "F",   "D#",    "D",    "C"};

	private static final double[] BFREQUENCIESm = {246.94, 220.00, 196.00, 185.00, 164.81, 146.83, 138.59, 123.47};
	private static final String[] BNAMEm        = {"B",    "A",    "G",    "F#",   "E",    "D",    "C#",   "B"};

	private static final double[] AsFREQUENCIESm = {233.08, 207.65, 185.00, 174.61, 155.56, 138.59, 130.81, 116.54};
	private static final String[] AsNAMEm        = {"A#",   "G#",   "F#",   "F",    "D#",   "C#",   "C",   "A#"};

	private static final double[] AFREQUENCIESm = {220.00, 196.00, 174.61, 164.81, 146.83, 130.81, 123.47, 110.00};
	private static final String[] ANAMEm        = {"A",    "G",    "F",    "E",    "D",    "C",    "B",    "A"};

	private static final double[] GsFREQUENCIESm = {207.65, 185.00, 164.81, 155.56, 138.59, 123.47, 116.54, 103.83};
	private static final String[] GsNAMEm        = {"G#",   "F#",   "E",    "D#",   "C#",   "B",    "A#",   "G#"};

	private static final double[] GFREQUENCIESm = {196.00, 174.61, 155.56, 146.83, 130.81, 116.54, 110.00, 98.00};
	private static final String[] GNAMEm        = {"G",    "F",    "D#",   "D",    "C",    "A#",   "A",    "G"};

	private static final double[] FsFREQUENCIESm = {185.00, 164.81, 146.83, 138.59, 123.47, 110.00, 103.83, 92.50};
	private static final String[] FsNAMEm        = {"F#",   "E",    "D",    "C#",   "B",    "A",    "G#",   "F#"};


	/**
	 * Returns the name array of chosen scale		
	 * @param root
	 * @param scale
	 * @return
	 */
	public static String[] getScale(String root, String scale){
		String[] ret = ENAME;

		if(scale == "Major"){
			if (root == "E"){
				ret = ENAME;	
			} else if (root == "F"){
				ret = FNAME;
			} else if (root == "F#"){
				ret = FNAME;
			} else if (root == "G"){
				ret = GNAME;
			} else if (root == "G#"){
				ret = GsNAME;
			} else if (root == "A"){
				ret = ANAME;
			} else if (root == "A#"){
				ret = AsNAME;
			} else if (root == "B"){
				ret = BNAME;
			} else if (root == "C"){
				ret = CNAME;
			} else if (root == "C#"){
				ret = CsNAME;
			} else if (root == "D"){
				ret = DNAME;
			} else if (root == "D#"){
				ret = DsNAME;
			}
		} else if (scale == "Minor"){
			if (root == "E"){
				ret = ENAMEm;	
			} else if (root == "F"){
				ret = FNAMEm;
			} else if (root == "F#"){
				ret = FNAMEm;
			} else if (root == "G"){
				ret = GNAMEm;
			} else if (root == "G#"){
				ret = GsNAMEm;
			} else if (root == "A"){
				ret = ANAMEm;
			} else if (root == "A#"){
				ret = AsNAMEm;
			} else if (root == "B"){
				ret = BNAMEm;
			} else if (root == "C"){
				ret = CNAMEm;
			} else if (root == "C#"){
				ret = CsNAMEm;
			} else if (root == "D"){
				ret = DNAMEm;
			} else if (root == "D#"){
				ret = DsNAMEm;
			}
		} 
		return ret;
	}

	/**
	 * Returns the frequency array of chosen scale
	 * @param root
	 * @param scale
	 * @return
	 */
	public static double[] getFreq(String root, String scale){
		double[] ret = EFREQUENCIES;

		if(scale == "Major"){
			if (root == "E"){
				ret = EFREQUENCIES;	
			} else if (root == "F"){
				ret = FFREQUENCIES;
			} else if (root == "F#"){
				ret = FsFREQUENCIES;
			} else if (root == "G"){
				ret = GFREQUENCIES;
			} else if (root == "G#"){
				ret = GsFREQUENCIES;
			} else if (root == "A"){
				ret = AFREQUENCIES;
			} else if (root == "A#"){
				ret = AsFREQUENCIES;
			} else if (root == "B"){
				ret = BFREQUENCIES;
			} else if (root == "C"){
				ret = CFREQUENCIES;
			} else if (root == "C#"){
				ret = CsFREQUENCIES;
			} else if (root == "D"){
				ret = DFREQUENCIES;
			} else if (root == "D#"){
				ret = DsFREQUENCIES;
			}
		} else if (scale == "Minor"){
			if (root == "E"){
				ret = EFREQUENCIESm;	
			} else if (root == "F"){
				ret = FFREQUENCIESm;
			} else if (root == "F#"){
				ret = FsFREQUENCIESm;
			} else if (root == "G"){
				ret = GFREQUENCIESm;
			} else if (root == "G#"){
				ret = GsFREQUENCIESm;
			} else if (root == "A"){
				ret = AFREQUENCIESm;
			} else if (root == "A#"){
				ret = AsFREQUENCIESm;
			} else if (root == "B"){
				ret = BFREQUENCIESm;
			} else if (root == "C"){
				ret = CFREQUENCIESm;
			} else if (root == "C#"){
				ret = CsFREQUENCIESm;
			} else if (root == "D"){
				ret = DFREQUENCIESm;
			} else if (root == "D#"){
				ret = DsFREQUENCIESm;
			}
		} 
		return ret;		
	}

}
