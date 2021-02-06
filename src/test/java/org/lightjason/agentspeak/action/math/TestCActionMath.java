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

import com.codepoetics.protonpack.StreamUtils;
import org.apache.commons.math3.primes.Primes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.lightjason.agentspeak.action.IAction;
import org.lightjason.agentspeak.language.CRawTerm;
import org.lightjason.agentspeak.language.ITerm;
import org.lightjason.agentspeak.language.execution.IContext;
import org.lightjason.agentspeak.testing.IBaseTest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * test math functions
 */
public final class TestCActionMath extends IBaseTest
{

    /**
     * data provider generator for single-value tests
     * @return data
     */
    public static Stream<Arguments> singlevaluegenerate()
    {
        return Stream.concat(

            singlevaluetestcase(

                Stream.of( 2.5, 9.1, 111.7, 889.9 ),

                Stream.of(
                    CNextPrime.class
                ),

                i -> (double) Primes.nextPrime( i.intValue() )
            ),

            singlevaluetestcase(

                Stream.of( -2, -6, 4, -1, -5, 3, 49, 30, 6, 5, 1.3, 2.8, 9.7, 1, 8, 180, Math.PI ),

                Stream.of(
                    CAbs.class,
                    CACos.class,
                    CASin.class,
                    CATan.class,
                    CCeil.class,
                    CCos.class,
                    CCosh.class,
                    CDegrees.class,
                    CExp.class,
                    CIsPrime.class,
                    CLog.class,
                    CLog10.class,
                    CFloor.class,
                    CRadians.class,
                    CRound.class,
                    CSignum.class,
                    CSin.class,
                    CSinh.class,
                    CSqrt.class,
                    CTan.class,
                    CTanh.class
                ),

                i -> Math.abs( i.doubleValue() ),
                i -> Math.acos( i.doubleValue() ),
                i -> Math.asin( i.doubleValue() ),
                i -> Math.atan( i.doubleValue() ),
                i -> Math.ceil( i.doubleValue() ),
                i -> Math.cos( i.doubleValue() ),
                i -> Math.cosh( i.doubleValue() ),
                i -> Math.toDegrees( i.doubleValue() ),
                i -> Math.exp( i.doubleValue() ),
                i -> Primes.isPrime( i.intValue() ),
                i -> Math.log( i.doubleValue() ),
                i -> Math.log10( i.doubleValue() ),
                i -> Math.floor( i.doubleValue() ),
                i -> Math.toRadians( i.doubleValue() ),
                i -> Math.round( i.doubleValue() ),
                i -> Math.signum( i.doubleValue() ),
                i -> Math.sin( i.doubleValue() ),
                i -> Math.sinh( i.doubleValue() ),
                i -> Math.sqrt( i.doubleValue() ),
                i -> Math.tan( i.doubleValue() ),
                i -> Math.tanh( i.doubleValue() )
            )

        );
    }


    /**
     * data provider generator for aggregation-value tests
     * @return data
     */
    public static Stream<Arguments> aggregationvaluegenerate()
    {
        final Random l_random = new Random();

        return aggregationvaluetestcase(

            IntStream.range( 0, 100 ).boxed().map( i -> l_random.nextGaussian() ),

            Stream.of(
                CAverage.class,
                CSum.class,
                CMin.class,
                CMax.class
            ),

            i -> i.mapToDouble( Number::doubleValue ).average().getAsDouble(),
            i -> i.mapToDouble( Number::doubleValue ).sum(),
            i -> i.mapToDouble( Number::doubleValue ).min().getAsDouble(),
            i -> i.mapToDouble( Number::doubleValue ).max().getAsDouble()

        );
    }


    /**
     * create test case
     *
     * @param p_input input data
     * @param p_class action class
     * @param p_result result function
     * @return test-case data
     */
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    private static Stream<Arguments> singlevaluetestcase( final Stream<Number> p_input, final Stream<Class<? extends IAction>> p_class,
                                                          final Function<Number, ?>... p_result )
    {
        final List<Object> l_input = p_input.collect( Collectors.toList() );

        return StreamUtils.zip(
            p_class,
            Arrays.stream( p_result ),
            ( i, j ) -> Arguments.of( l_input, i, j )
        );
    }


    /**
     * create test case
     *
     * @param p_input input data
     * @param p_class action class
     * @param p_result result function
     * @return test-case data
     */
    @SafeVarargs
    @SuppressWarnings( "varargs" )
    private static Stream<Arguments> aggregationvaluetestcase( final Stream<Number> p_input, final Stream<Class<? extends IAction>> p_class,
                                                               final Function<Stream<Number>, ?>... p_result )
    {
        final List<Object> l_input = p_input.collect( Collectors.toList() );

        return StreamUtils.zip(
            p_class,
            Arrays.stream( p_result ),
            ( i, j ) -> Arguments.of( l_input, i, j )
        );
    }


    /**
     * test all aggregation-value actions
     *
     * @param p_input test input data
     * @param p_action action
     * @param p_result result arguments
     *
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrwon on instantiation error
     */
    @ParameterizedTest
    @MethodSource( "aggregationvaluegenerate" )
    public void aggregationvalueaction( final List<Object> p_input, final Class<? extends IAction> p_action, final Function<Stream<Number>, ?> p_result )
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_action.getConstructor().newInstance().execute(
            false, IContext.EMPTYPLAN,
            p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( p_result.apply( p_input.stream().map( i -> (Number)i ) ), l_return.get( 0 ).raw() );
    }


    /**
     * test all single-value actions
     *
     * @param p_input input test data
     * @param p_action action
     * @param p_result result arguments
     *
     * @throws IllegalAccessException is thrwon on instantiation error
     * @throws InstantiationException is thrwon on instantiation error
     * @throws NoSuchMethodException is thrwon on instantiation error
     * @throws InvocationTargetException is thrwon on instantiation error
     */
    @ParameterizedTest
    @MethodSource( "singlevaluegenerate" )
    public void singlevalueaction( final List<Object> p_input, final Class<? extends IAction> p_action, final Function<Number, ?> p_result )
        throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException
    {
        final List<ITerm> l_return = new ArrayList<>();

        p_action.getConstructor().newInstance().execute(
            false, IContext.EMPTYPLAN,
            p_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals(
            p_input.stream().map( i -> (Number)i ).map( p_result ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray(),
            p_action.toGenericString()
        );
    }


    /**
     * test binomial
     */
    @Test
    public void binomial()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CBinomial().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 49, 30, 6, 5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 18851684897584L, l_return.get( 0 ).<Number>raw() );
        Assertions.assertEquals( 6L, l_return.get( 1 ).<Number>raw() );
    }


    /**
     * test factorial
     */
    @Test
    public void factorial()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CFactorial().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 5, 1, 2, 3, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 120L, l_return.get( 0 ).<Number>raw() );
        Assertions.assertEquals( 1L, l_return.get( 1 ).<Number>raw() );
        Assertions.assertEquals( 2L, l_return.get( 2 ).<Number>raw() );
        Assertions.assertEquals( 6L, l_return.get( 3 ).<Number>raw() );
        Assertions.assertEquals( 24L, l_return.get( 4 ).<Number>raw() );
    }


    /**
     * test primefactors
     */
    @Test
    public void primefactors()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPrimeFactors().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 8, 120 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals( Stream.of( 2D, 2D, 2D ).toArray(), l_return.get( 0 ).<List<?>>raw().toArray() );
        Assertions.assertArrayEquals( Stream.of( 2D, 2D, 2D, 3D, 5D ).toArray(), l_return.get( 1 ).<List<?>>raw().toArray() );
    }


    /**
     * test sigmoid
     */
    @Test
    public void sigmoid()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CSigmoid().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1, 1, 1, 10, 20, 30 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 0.9999546021312976, l_return.get( 1 ).<Number>raw() );
        Assertions.assertEquals( 0.9999999979388463, l_return.get( 2 ).<Number>raw() );
        Assertions.assertEquals( 0.9999999999999065, l_return.get( 3 ).<Number>raw() );
    }


    /**
     * test stirling
     */
    @Test
    public void stirling()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CStirling().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3, 2, 8, 3 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals( Stream.of( 3L, 966L ).toArray(), l_return.stream().map( ITerm::<Number>raw ).toArray() );
    }


    /**
     * test power
     */
    @Test
    public void pow()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CPow().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 2, 3, 4, 0.5 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals(
            Stream.of( 9.0, 16.0, 0.25 ).toArray(),
            l_return.stream().map( ITerm::raw ).toArray()
        );
    }


    /**
     * test geometricmean
     */
    @Test
    public void geometricmean( )
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CGeometricMean().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 1.05, 1.03, 0.94, 1.02, 1.04 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( 1.0152139522031014, l_return.get( 0 ).<Number>raw() );
    }


    /**
     * test harmonicmean
     */
    @Test
    public void harmonicmean()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CHarmonicMean().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 150, 50 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( 75.0, l_return.get( 0 ).<Number>raw() );
    }

    /**
     * test hypot
     */
    @Test
    public void hypot()
    {
        final Random l_random = new Random();
        final List<Double> l_input = IntStream.range( 0, 100 ).mapToDouble( i -> l_random.nextGaussian() ).boxed().collect( Collectors.toList() );

        final List<ITerm> l_return = new ArrayList<>();

        new CHypot().execute(
            false, IContext.EMPTYPLAN,
            l_input.stream().map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertArrayEquals(
            StreamUtils.windowed( l_input.stream(), 2, 2 )
                       .mapToDouble( i -> Math.hypot( i.get( 0 ), i.get( 1 ) ) )
                       .boxed()
                       .toArray(),
            l_return.stream().map( ITerm::<Number>raw ).toArray()
        );
    }


    /**
     * test max index
     */
    @Test
    public void maxIndex()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMaxIndex().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3, 4, 9, 1, 7, 8, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( 2D, l_return.get( 0 ).<Number>raw() );
    }

    /**
     * test min index
     */
    @Test
    public void minIndex()
    {
        final List<ITerm> l_return = new ArrayList<>();

        new CMinIndex().execute(
            false, IContext.EMPTYPLAN,
            Stream.of( 3, 4, 9, 1, 7, 8, 4 ).map( CRawTerm::of ).collect( Collectors.toList() ),
            l_return
        );

        Assertions.assertEquals( 1, l_return.size() );
        Assertions.assertEquals( 3D, l_return.get( 0 ).<Number>raw() );
    }

}
