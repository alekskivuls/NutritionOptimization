import org.gnu.glpk.GLPK;
import org.gnu.glpk.GLPKConstants;
import org.gnu.glpk.SWIGTYPE_p_double;
import org.gnu.glpk.SWIGTYPE_p_int;
import org.gnu.glpk.glp_prob;
import org.gnu.glpk.glp_smcp;

//import org.gnu.glpk.glp_intopt;
//import org.gnu.glpk.glp_iocp;

public class Nutrition {

	// Maximize z = 17 * x1 + 12* x2
	// subject to
	// 10*x1+7*x2 <= 40
	// x1+ x2 <= 5
	// where,
	// 0.0 <= x1 integer
	// 0.0 <= x2 integer
	// result: max=68: x1=4, x2=0; ??

	static {
		try {
			// try to load Linux library
			System.loadLibrary("glpk_java");
		} catch (UnsatisfiedLinkError e) {
			// try to load Windows library
			System.load("C:\\Users\\akivuls\\Downloads\\winglpk-4.60\\glpk-4.60\\w64\\glpk_4_60.dll");
		}
	}

	public static void main(String[] arg) {
		glp_prob mip;
		glp_smcp parm;
		SWIGTYPE_p_int ind;
		SWIGTYPE_p_double val;
		int ret;

		// Create problem
		mip = GLPK.glp_create_prob();
		System.out.println("Problem created");
		GLPK.glp_set_prob_name(mip, "myProblem");

		// Define columns
		GLPK.glp_add_cols(mip, 2);
		GLPK.glp_set_col_name(mip, 1, "x1");
		GLPK.glp_set_col_kind(mip, 1, GLPKConstants.GLP_IV);
		GLPK.glp_set_col_bnds(mip, 1, GLPKConstants.GLP_LO, 0, 0);
		GLPK.glp_set_col_name(mip, 2, "x2");
		GLPK.glp_set_col_kind(mip, 2, GLPKConstants.GLP_IV);
		GLPK.glp_set_col_bnds(mip, 2, GLPKConstants.GLP_LO, 0, 0);

		// Create constraints
		GLPK.glp_add_rows(mip, 3);
		GLPK.glp_set_row_name(mip, 1, "c1");
		GLPK.glp_set_row_bnds(mip, 1, GLPKConstants.GLP_UP, 0, 40);
		ind = GLPK.new_intArray(3);
		GLPK.intArray_setitem(ind, 1, 1);
		GLPK.intArray_setitem(ind, 2, 2);
		val = GLPK.new_doubleArray(3);
		GLPK.doubleArray_setitem(val, 1, 10);
		GLPK.doubleArray_setitem(val, 2, 7);
		GLPK.glp_set_mat_row(mip, 1, 2, ind, val);

		//
		GLPK.glp_set_row_name(mip, 2, "c2");
		GLPK.glp_set_row_bnds(mip, 2, GLPKConstants.GLP_UP, 0, 5);
		ind = GLPK.new_intArray(3);
		GLPK.intArray_setitem(ind, 1, 1);
		GLPK.intArray_setitem(ind, 2, 2);
		val = GLPK.new_doubleArray(3);
		GLPK.doubleArray_setitem(val, 1, 1);
		GLPK.doubleArray_setitem(val, 2, 1);
		GLPK.glp_set_mat_row(mip, 2, 2, ind, val);

		// Define objective
		GLPK.glp_set_obj_name(mip, "z");
		GLPK.glp_set_obj_dir(mip, GLPKConstants.GLP_MAX);

		// GLPK.glp_set_obj_coef(mip, 0, 0);
		GLPK.glp_set_obj_coef(mip, 1, 17);
		GLPK.glp_set_obj_coef(mip, 2, 12);

		// solve model
		parm = new glp_smcp();
		GLPK.glp_init_smcp(parm);
		ret = GLPK.glp_simplex(mip, parm);

		// Retrieve solution
		if (ret == 0) {
			write_lp_solution(mip);
		} else {
			System.out.println("The problem could not be solved");
		}
		;
		// free memory
		GLPK.glp_delete_prob(mip);
	}

	/**
	 *
	 * write simplex solution
	 *
	 * @param lp
	 *            problem
	 *
	 */

	static void write_lp_solution(glp_prob mip) {
		int i;
		int n;
		String name;
		double val;
		// int val;
		name = GLPK.glp_get_obj_name(mip);
		val = GLPK.glp_get_obj_val(mip);
		// val = GLPK.glp_mip_obj_val(mip);

		System.out.print(name);
		System.out.print(" = ");
		System.out.println(val);
		n = GLPK.glp_get_num_cols(mip);

		for (i = 1; i <= n; i++) {
			name = GLPK.glp_get_col_name(mip, i);
			val = GLPK.glp_get_col_prim(mip, i);
			// val = GLPK.glp_mip_col_val(mip, i);

			System.out.print(name);
			System.out.print(" = ");
			System.out.println(val);
		}
	}

}