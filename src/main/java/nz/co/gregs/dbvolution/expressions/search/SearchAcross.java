/*
 * Copyright 2019 Gregory Graham.
 *
 * Commercial licenses are available, please contact info@gregs.co.nz for details.
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/4.0/ 
 * or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.
 * 
 * You are free to:
 *     Share - copy and redistribute the material in any medium or format
 *     Adapt - remix, transform, and build upon the material
 * 
 *     The licensor cannot revoke these freedoms as long as you follow the license terms.               
 *     Under the following terms:
 *                 
 *         Attribution - 
 *             You must give appropriate credit, provide a link to the license, and indicate if changes were made. 
 *             You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
 *         NonCommercial - 
 *             You may not use the material for commercial purposes.
 *         ShareAlike - 
 *             If you remix, transform, or build upon the material, 
 *             you must distribute your contributions under the same license as the original.
 *         No additional restrictions - 
 *             You may not apply legal terms or technological measures that legally restrict others from doing anything the 
 *             license permits.
 * 
 * Check the Creative Commons website for any details, legalese, and updates.
 */
package nz.co.gregs.dbvolution.expressions.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nz.co.gregs.dbvolution.expressions.BooleanExpression;
import nz.co.gregs.dbvolution.expressions.NumberExpression;
import nz.co.gregs.dbvolution.expressions.SortProvider;
import nz.co.gregs.dbvolution.expressions.StringExpression;

/**
 * Standardised searching using string terms and expression aliases.
 *
 * <p>
 * Designed to provide easy access to complex user-driven searching such as
 * 'terminator -schwarzenagger "come with me if" desc:quote author:+"james
 * cameron"'.</p>
 *
 * <p>
 * Search terms can be single words or sequence, or quoted phrases. Terms can
 * also be prioritized with + and - and restricted to a single column using an
 * alias followed by a colon (alias:term). Searching for any empty value can be
 * done with an alias followed by empty quotes, for example description:""</p>
 *
 * <p>
 * Use with a single column using {@link StringExpression#searchFor(nz.co.gregs.dbvolution.expressions.search.SearchString)
 * } and {@link StringExpression#searchForRanking(nz.co.gregs.dbvolution.expressions.search.SearchString)
 * }: marq.column(marq.name).searchFor(searchString). If you have individual
 * strings use
 * {@link StringExpression#searchFor(java.lang.String...) and {@link StringExpression#searchForRanking(java.lang.String...) }.</p>
 *
 * <p>
 * searchForRanking produces a number value that can be used for sorting. </p>
 *
 * @author gregorygraham
 */
public class SearchAcross extends SearchAbstract {

	private final List<ExpressionAlias> columnsToSearch = new ArrayList<>();

	public SearchAcross() {
		setSearchString("");
	}
	
	public SearchAcross(String searchTerms, ExpressionAlias... columns) {
		setSearchString(searchTerms);
		this.columnsToSearch.addAll(Arrays.asList(columns));
	}

	public BooleanExpression getComparisonExpression() {
		return getRankingExpression().isGreaterThan(0);
	}

	public NumberExpression getRankingExpression() {
		NumberExpression theExpr = NumberExpression.value(0.0);
		for (ExpressionAlias col : columnsToSearch) {
			theExpr = theExpr.plus(this.getRankingExpression(col));
		}
		return theExpr.plus(0).bracket();
	}

	public SearchAcross andSearchAcross(StringExpression column, String name) {
		this.columnsToSearch.add(new ExpressionAlias(column, name));
		return this;
	}

	public SortProvider ascending() {
		return this.getRankingExpression().ascending();
	}

	public SortProvider descending() {
		return this.getRankingExpression().descending();
	}

	@Override
	public SearchAcross add(String string) {
		super.add(string);
		return this;
	}

	@Override
	public SearchAcross addAliasedTerm(String string, String alias) {
		super.addAliasedTerm(string, alias);
		return this;
	}

	@Override
	public SearchAcross addQuotedTerm(String string) {
		super.addQuotedTerm(string);
		return this;
	}

	@Override
	public SearchAcross addPreferredTerm(String string) {
		super.addPreferredTerm(string);
		return this;
	}

	@Override
	public SearchAcross addReducedTerm(String string) {
		super.addReducedTerm(string);
		return this;
	}

}
