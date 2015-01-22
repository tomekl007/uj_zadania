package zad6;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiStageExecutorTest {
  class OneStageWork implements OneStageWorkInterface {

    private int stage;

    @Override
    public void work(int stage) {
      System.out.println("work : " + this + " "  + stage);
      this.stage = stage;
    }


  }

  @Test
  public void shouldRegisterWork() {
    //given
    OneStageWork oneStageWorkFirst = new OneStageWork();
    oneStageWorkFirst.work(2);
    OneStageWork oneStageWorkSecond = new OneStageWork();
    oneStageWorkSecond.work(1);
    OneStageWork oneStageWorkThird = new OneStageWork();
    oneStageWorkThird.work(1);
    MultiStageExecutor multiStageExecutor = new MultiStageExecutor();
    //when
    multiStageExecutor.register(oneStageWorkFirst.stage, oneStageWorkFirst);
    multiStageExecutor.register(oneStageWorkSecond.stage, oneStageWorkSecond);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkThird);
    //then
    assertThat(multiStageExecutor.stages.size()).isEqualTo(2);
    assertThat(multiStageExecutor.stages.get(1).size()).isEqualTo(2);
  }

  @Test
  public void shouldDeregisterWorkSingle(){
    //given
    OneStageWork oneStageWorkFirst = new OneStageWork();
    oneStageWorkFirst.work(2);
    OneStageWork oneStageWorkSecond = new OneStageWork();
    oneStageWorkSecond.work(1);
    OneStageWork oneStageWorkThird = new OneStageWork();
    oneStageWorkThird.work(1);
    MultiStageExecutor multiStageExecutor = new MultiStageExecutor();
    //when
    multiStageExecutor.register(oneStageWorkFirst.stage, oneStageWorkFirst);
    multiStageExecutor.register(oneStageWorkSecond.stage, oneStageWorkSecond);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkThird);
    multiStageExecutor.deregister(1, oneStageWorkSecond);
    //then
    assertThat(multiStageExecutor.stages.size()).isEqualTo(2);
    assertThat(multiStageExecutor.stages.get(1).size()).isEqualTo(1);
  }

  @Test
  public void shouldDeregisterWorkForAllStages(){
    //given
    OneStageWork oneStageWorkFirst = new OneStageWork();
    oneStageWorkFirst.work(2);
    OneStageWork oneStageWorkSecond = new OneStageWork();
    oneStageWorkSecond.work(1);
    OneStageWork oneStageWorkThird = new OneStageWork();
    oneStageWorkThird.work(1);
    MultiStageExecutor multiStageExecutor = new MultiStageExecutor();
    //when
    multiStageExecutor.register(1, oneStageWorkFirst);
    multiStageExecutor.register(3, oneStageWorkFirst);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkThird);
    multiStageExecutor.deregister(oneStageWorkFirst);
    //then
    assertThat(multiStageExecutor.stages.get(3).size()).isEqualTo(0);
    assertThat(multiStageExecutor.stages.get(1).size()).isEqualTo(1);
  }

  @Test
  public void shouldGetMaxValueOfStage(){
    //given
    OneStageWork oneStageWorkFirst = new OneStageWork();
    oneStageWorkFirst.work(4);
    OneStageWork oneStageWorkSecond = new OneStageWork();
    int maxValue = 99;
    oneStageWorkSecond.work(maxValue);
    OneStageWork oneStageWorkThird = new OneStageWork();
    oneStageWorkThird.work(14);
    MultiStageExecutor multiStageExecutor = new MultiStageExecutor();
    //when
    multiStageExecutor.register(oneStageWorkFirst.stage, oneStageWorkFirst);
    multiStageExecutor.register(oneStageWorkSecond.stage, oneStageWorkSecond);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkThird);
    //then
    assertThat(multiStageExecutor.getNumerOfStages()).isEqualTo(maxValue);
  }
  @Test
     public void shouldExecuteAllWork(){
    //given
    OneStageWork oneStageWorkFirst = new OneStageWork();
    oneStageWorkFirst.work(4);
    OneStageWork oneStageWorkSecond = new OneStageWork();
    int maxValue = 99;
    oneStageWorkSecond.work(maxValue);
    OneStageWork oneStageWorkThird = new OneStageWork();
    oneStageWorkThird.work(14);
    MultiStageExecutor multiStageExecutor = new MultiStageExecutor();
    //when
    multiStageExecutor.register(oneStageWorkFirst.stage, oneStageWorkFirst);
    multiStageExecutor.register(oneStageWorkSecond.stage, oneStageWorkSecond);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkThird);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkSecond);
    multiStageExecutor.execute();
  }


  @Test
  public void shouldIgnoreDeregisteringNotExistingObject(){
    //given
    OneStageWork oneStageWorkFirst = new OneStageWork();
    oneStageWorkFirst.work(2);
    OneStageWork oneStageWorkSecond = new OneStageWork();
    oneStageWorkSecond.work(1);
    OneStageWork oneStageWorkThird = new OneStageWork();
    oneStageWorkThird.work(1);
    MultiStageExecutor multiStageExecutor = new MultiStageExecutor();
    //when
    multiStageExecutor.register(oneStageWorkFirst.stage, oneStageWorkFirst);
    multiStageExecutor.register(oneStageWorkSecond.stage, oneStageWorkSecond);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkThird);
    multiStageExecutor.deregister(oneStageWorkFirst.stage, new OneStageWork());
    //then
    assertThat(multiStageExecutor.stages.size()).isEqualTo(2);
    assertThat(multiStageExecutor.stages.get(1).size()).isEqualTo(2);
  }



  @Test
  public void shouldIgnoreDeregisteringForWrongStage(){
    //given
    OneStageWork oneStageWorkFirst = new OneStageWork();
    oneStageWorkFirst.work(2);
    OneStageWork oneStageWorkSecond = new OneStageWork();
    oneStageWorkSecond.work(1);
    OneStageWork oneStageWorkThird = new OneStageWork();
    oneStageWorkThird.work(1);
    MultiStageExecutor multiStageExecutor = new MultiStageExecutor();
    //when
    multiStageExecutor.register(oneStageWorkFirst.stage, oneStageWorkFirst);
    multiStageExecutor.register(oneStageWorkSecond.stage, oneStageWorkSecond);
    multiStageExecutor.register(oneStageWorkThird.stage, oneStageWorkThird);
    multiStageExecutor.deregister(99, oneStageWorkThird);
    //then
    assertThat(multiStageExecutor.stages.size()).isEqualTo(2);
    assertThat(multiStageExecutor.stages.get(1).size()).isEqualTo(2);
  }

}