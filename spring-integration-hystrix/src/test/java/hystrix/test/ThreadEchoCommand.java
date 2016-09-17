package hystrix.test;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;


/**
 * ���󻺴棨Request Cache���������û�����HystrixCommandʱ��HystrixCommandֱ�Ӵӻ�����ȡ������Ҫ�����ⲿ����HystrixCommand�ӻ�����ȡ��Ҫ3�������� 
1. ��HystrixCommand������һ��HystrixRequestContext�� 
2. ��HystrixCommandʵ����getCacheKey()���� 
3. ��HystrixRequestContext��������ͬCache Keyֵ�Ļ��� 
 * @author Administrator
 *
 */
public class ThreadEchoCommand extends HystrixCommand<String> {
	private String input;

	protected ThreadEchoCommand(String input) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("EchoGroup"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("Echo"))
				//.andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("EchoThreadPool"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
						.withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(5)));
		this.input = input;
	}
	
	protected ThreadEchoCommand(String input,Setter setter) {
		super(setter);
		this.input = input;
	}
	
	
	/**
	 * �����ʼ�� HystrixRequestContext context = HystrixRequestContext.initializeContext();
	 */
	/*@Override
	protected String getCacheKey() {
		return input;
	}*/

	public static void flushCache(String cacheKey) {
		HystrixRequestCache
				.getInstance(HystrixCommandKey.Factory.asKey("Echo"), HystrixConcurrencyStrategyDefault.getInstance())
				.clear(cacheKey);
	}

	@Override
	protected String run() throws Exception {
		return "Echo: " + input;
	}

}