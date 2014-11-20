import com.carrotsearch.junitbenchmarks.AbstractBenchmark;

import static org.junit.Assert.assertEquals;



/**
 * @author Tomasz Lelek
 * @since 2014-11-20
 */
public class PercolationTestSupplied extends AbstractBenchmark{
  private boolean[][] fixture_BigLab_OK = {
    {true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true,},
    {
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},

    {
      true, false, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, false,}, // last one here is exit
    {
      true, false, true, false, true, false, false, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, false, true,},
    {
      true, false, false, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, true, true, false, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true, false, true, false,
      true, false, false, false, true, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, true, false,
      true, false, false, false, true, false, true, false, true, false, false, false,
      true, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, true, true, false, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, false, true,},
    {
      true, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, true, true, false, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, true, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, true, false, false, false,
      true, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, true, false, false, false, false, false, true, false,
      false, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, true,
      true, true, true, true, true, false, false, false, true,},
    {
      true, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, true, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, false, false, true,}
  };

  private boolean[][] fixture_BigLab_FAIL = {
    {true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true,},
    {
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},

    {
      true, false, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, true}, // last one here is exit
    {
      true, false, true, false, true, false, false, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, true, false, true, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, false, true,},
    {
      true, false, false, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, false, true, false, true, false, true, false,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, false, true, true, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, true, false,
      true, false, false, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, true, true, false, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true, false, true, false,
      true, false, false, false, true, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, true, false, false, false,
      false, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, true, false,
      true, false, false, false, true, false, true, false, true, false, false, false,
      true, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, true,
      true, true, true, true, true, false, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, true, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, false, true, false, true, true, true, true, true, false,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, false, false,
      false, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, false,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, true, true, false, true,},
    {
      true, false, false, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, false, false, false, false, true, false,
      true, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      true, false, true, false, true, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, false, false, false, false, true, false, true,},
    {
      true, false, true, false, true, false, true, false, true, true, true, false,
      true, true, true, false, true, true, true, true, true, false, true, false,
      true, true, true, true, true, false, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, true,
      true, true, true, false, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, true, true, true, true, true, true, false, true,},
    {
      true, false, true, false, true, false, false, false, true, false, false, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, true, true, false, true, false, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, true, true, false, true, false, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, false, false, true, false, true, false,
      false, false, false, false, true, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, true, false, true, false,
      false, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, true, true, true, true, false,
      true, false, true, false, true, true, true, false, true, false, true, false,
      true, false, true, false, true, false, true, true, true, false, true, true,
      true, false, true, false, true, false, true, true, true, false, true, false,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, false, true, false, true, false, true, false, true,},
    {
      true, false, true, false, true, false, false, false, false, false, false, false,
      true, false, false, false, false, false, true, false, false, false, true, false,
      true, false, true, false, false, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, true, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, true, false, false, false,
      true, false, false, false, true, false, true, false, true,},
    {
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, true, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, false, true,},
    {
      true, false, false, false, true, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true, false, false, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, true, false, true, false, false, false, false, false, true, false,
      false, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      false, false, true, false, false, false, true, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, false, true, false,
      true, false, true, false, true, true, true, false, true, false, true, true,
      true, false, true, false, true, false, true, true, true, true, true, true,
      true, true, true, false, true, true, true, true, true, false, true, true,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, false, true, true, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, true, false,
      false, false, true, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, true, false, false, false, true, false,
      true, false, true, false, true, false, true, false, false, false, false, false,
      false, false, true, false, false, false, true, false, false, false, false, false,
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, false, false, true, false, false, false, true,},
    {
      true, false, true, true, true, false, true, true, true, false, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, false, true, true, true, false, true, false, true, true, true, false,
      true, true, true, false, true, false, true, false, true, true, true, true,
      true, true, true, false, true, false, true, true, true, false, true, true,
      true, false, true, true, true, true, true, false, true, true, true, true,
      true, true, true, true, true, false, false, false, true,},
    {
      true, false, false, false, false, false, false, false, true, false, true, false,
      true, false, false, false, true, false, false, false, true, false, true, false,
      true, false, true, false, true, false, false, false, true, false, false, false,
      false, false, false, false, false, false, true, false, true, false, false, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, true, false, false, false,
      false, false, false, false, false, false, false, false, true,},
    {
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, false, true, false, true, true, true, false, true, false,
      true, false, true, false, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, false, true, false, true, true,
      true, false, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, false, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, true, false, false, false, true, false, false, false, false, false,
      false, false, true, false, true, false, true, false, false, false, true, false,
      true, false, false, false, false, false, false, false, false, false, true, false,
      false, false, false, false, false, false, false, false, false, false, true, false,
      true, false, true, false, false, false, false, false, false, false, false, false,
      true, false, true, false, true, false, true, false, false, false, true, false,
      false, false, false, false, true, false, true, false, true,},
    {
      true, true, true, false, true, true, true, false, true, true, true, true,
      true, false, true, true, true, true, true, false, true, true, true, false,
      true, true, true, true, true, true, true, true, true, false, true, true,
      true, true, true, true, true, true, true, true, true, true, true, false,
      true, true, true, false, true, true, true, true, true, true, true, true,
      true, false, true, true, true, false, true, true, true, true, true, false,
      true, true, true, true, true, false, true, false, true,},
    {
      true, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, true, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      false, false, false, false, false, false, false, false, false, false, false, false,
      true, false, false, false, false, false, false, false, true,}
  };

  private boolean[][][] fixture4_OK = {
    {
      {false},
      {false},
      {false}
    },
    {
      {false, false, false}
    },
    {
      {false, true},
      {false, false},
    },
    {
      {false, true},
      {false, true},
      {false, true},
    },
    {
      {false, true, true, true},
      {false, false, true, true},
      {false, false, false, true},
      {false, true, false, false},
      {true, false, false, true},
      {true, false, true, true},
    },
    {
      {false, false, false, false, false, true},
      {true, false, true, true, false, true},
      {true, false, false, false, false, true},
      {true, true, true, true, false, false},
      {false, false, false, false, true, true},
    },
    {
      {false, false, false, false, true},
      {false, true, false, false, true},
      {false, false, false, false, true},
      {false, false, true, false, false}
    },
    {
      // first test from http://zti.if.uj.edu.pl/Piotr.Oramus/dydaktyka/PO.cwiczenia/Zadania/
      {true, true, true, true, false, false},
      {false, false, false, false, true, false},
      {false, true, true, false, false, false},
      {false, false, false, true, true, false},
      {false, true, false, false, false, true},
      {true, false, true, false, false, false},
      {false, false, false, true, true, false},
      {false, true, false, false, false, false}
    },
    fixture_BigLab_OK
  };

  private boolean[][][] fixture4_FAIL = {
//    {
//      {false}
//    },
    {
      {true}
    },
    {
      {false, true, false}
    },
    {
      {false},
      {true},
      {false}
    },
    {
      {false, true, true},
      {false, true, true},
    },
    {
      {false, true, false},
      {true, false, false}
    },
    {
      {false, true, false, true, false},
      {false, false, false, false, true},
      {true, true, false, true, true},
      {false, false, false, false, true},
    },
    fixture_BigLab_FAIL
  };

  private boolean[][][] fixture8_OK = {
    {
      {true, false, true},
      {false, true, false}
    },
    {
      {false, true, false},
      {true, false, true}
    },
    {
      {false, true},
      {true, false},
      {false, true}
    },
    {
      {true, false},
      {false, true},
      {true, false}
    },
    {
      {false},
      {false},
      {false}
    },
    {
      {false, false, false}
    },
    {
      {false, true, false, false},
      {true, false, true, false},
      {true, true, true, false},
      {true, false, false, true},
      {false, true, true, true},
      {true, false, false, true},
      {true, true, true, false},
    },
    {
      // second test from zti.if.uj.edu.pl/Piotr.Oramus/dydaktyka/PO.cwiczenia/Zadania/
      {true, false, false, false, true, false, false, false, false},
      {false, true, false, false, true, false, false, false, false},
      {false, true, true, false, false, true, false, false, false},
      {false, false, false, true, false, true, false, false, true},
      {false, true, false, true, false, true, false, true, false},
      {true, false, false, true, false, false, true, false, false}
    },
    fixture_BigLab_OK
  };


  private boolean[][][] fixture8_FAIL = {
    {
      {false, true, false}
    },
    {
      {false},
      {true},
      {false},
    },
    {
      {false, true, true},
      {true, false, true}
    },
    {
      {false, true},
      {true, false},
      {true, true}
    },
    fixture_BigLab_FAIL
  };

  @org.junit.Test
  public void testNeighbour4() throws Exception {
    for (int i = 0; i < fixture4_OK.length; ++i)
      assertEquals(i + " supposed to pass", true, Percolation.neighbors4(fixture4_OK[i]));
    for (int i = 0; i < fixture4_FAIL.length; ++i)
      assertEquals(i + " supposed to fail", false, Percolation.neighbors4(fixture4_FAIL[i]));
  }

  @org.junit.Test
  public void testNeighbour8() throws Exception {
    for (int i = 0; i < fixture8_OK.length; ++i)
      assertEquals(i + " supposed to pass", true, Percolation.neighbors8(fixture8_OK[i]));
    for (int i = 0; i < fixture8_FAIL.length; ++i)
      assertEquals(i + " supposed to fail", false, Percolation.neighbors8(fixture8_FAIL[i]));
  }
}
