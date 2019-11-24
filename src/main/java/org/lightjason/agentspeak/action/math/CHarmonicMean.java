/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason                                                #
 * # Copyright (c) 2015-19, LightJason (info@lightjason.org)                            #
 * # This program is free software: you can redistribute it and/or modify               #
 * # it under the terms of the GNU Lesser General Public License as                     #
 * # published by the Free Software Foundation, either version 3 of the                 #
 * # License, or (at your option) any later version.                                    #
 * #                                                                                    #
 * # This program is distributed in the hope that it will be useful,                    #
 * # but WITHOUT ANY WARRANTY; without even the implied warranty of                     #
 * # MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                      #
 * # GNU Lesser General Public License for more details.                                #
 * #                                                                                    #
 * # You should have received a copy of the GNU Lesser General Public License           #
 * # along with this program. If not, see http://www.gnu.org/licenses/                  #
 * ######################################################################################
 * @endcond
 */

package org.lightjason.agentspeak.action.math;

import org.lightjason.agentspeak.action.IBaseAction;
import org.lightjason.agentspeak.common.IPath;
import org.lightjason.agentspeak.language.CCommon;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.language.fuzzy.IFuzzyValue;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Stream;


/**
 * action for harmonic mean.
 * The action calculates \f$ \frac{i}{\sum_{i} \frac{1}{x_i}} \f$
 * over all unflatten arguments
 *
 * {@code G = .math/harmonicmean( 1, 3, 9, [10, [11, 12]] );}
 *
 * @see <a href="https://en.wikipedia.org/wiki/Average"></a>
 */
public final class CHarmonicMean extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 512098153369001644L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CHarmonicMean.class, "math" );

    @Nonnull
    @Override
    public IPath name()
    {
        return NAME;
    }

    @Nonnegative
    @Override
    public int minimalArgumentNumber()
    {
        return 1;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        p_return.add(
            CRawTerm.of(
                CHarmonicMean.apply(
                    CCommon.flatten( p_argument ).count(),
                    CCommon.flatten( p_argument )
                           .map( ITerm::<Number>raw )
                           .mapToDouble( Number::doubleValue )
                           .boxed()
                )
            )
        );

        return Stream.empty();
    }


    /**
     * calculate harmonic mean
     *
     * @param p_count number of elements
     * @param p_values value stream
     * @return harmonic mean
     */
    private static double apply( final double p_count, final Stream<Double> p_values )
    {
        return p_count / p_values.mapToDouble( i -> 1.0 / i ).sum();
    }

}
