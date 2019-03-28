/*
 * @cond LICENSE
 * ######################################################################################
 * # LGPL License                                                                       #
 * #                                                                                    #
 * # This file is part of the LightJason AgentSpeak(L++)                                #
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
import java.util.function.Function;
import java.util.stream.Stream;


/**
 * action for calculating a parameterized sigmoid function.
 * Action calculates the sigmoid function for each value, the definition
 * of the function is \f$ \frac{\alpha}{ \beta + e^{ - \gamma \cdot t }} \f$
 * \f$ \alpha \f$ is the first, \f$ \beta \f$ the second and \f$ \gamma \f$ the third
 * argument, all values beginning at the fourth position will be used for t, so the
 * action returns all values
 *
 * {@code [A | B | C] = .math/sigmoid( 1, 1, 1, 10, 20, 30 );}
 *
 * @see https://en.wikipedia.org/wiki/Sigmoid_function
 */
public final class CSigmoid extends IBaseAction
{
    /**
     * serial id
     */
    private static final long serialVersionUID = 3824593010060693544L;
    /**
     * action name
     */
    private static final IPath NAME = namebyclass( CSigmoid.class, "math" );

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
        return 3;
    }

    @Nonnull
    @Override
    public Stream<IFuzzyValue<?>> execute( final boolean p_parallel, @Nonnull final IContext p_context,
                                           @Nonnull final List<ITerm> p_argument, @Nonnull final List<ITerm> p_return
    )
    {
        final Function<Double, Double> l_sigmoid = i -> p_argument.get( 0 ).<Number>raw().doubleValue()
                                                        / ( p_argument.get( 1 ).<Number>raw().doubleValue()
                                                            + Math.exp( -p_argument.get( 2 ).<Number>raw().doubleValue() * i )
                                                        );

        CCommon.flatten( p_argument )
               .skip( 2 )
               .map( ITerm::<Number>raw )
               .mapToDouble( Number::doubleValue )
               .boxed()
               .map( l_sigmoid )
               .map( CRawTerm::of )
               .forEach( p_return::add );

        return Stream.of();
    }

}
