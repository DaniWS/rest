/* Copyright 2018-2021 Universidad Politécnica de Madrid (UPM).
 *
 * Authors:
 *    Daniel Vilela García
 *    José-Fernan Martínez Ortega
 *    Vicente Hernández Díaz
 * 
 * This software is distributed under a dual-license scheme:
 *
 * - For academic uses: Licensed under GNU Affero General Public License as
 *                      published by the Free Software Foundation, either
 *                      version 3 of the License, or (at your option) any
 *                      later version.
 * 
 * - For any other use: Licensed under the Apache License, Version 2.0.
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 
 * You can get a copy of the license terms in licenses/LICENSE.
 * 
 */

/**
 * Thread-safe cache for registration information retrieved from the Assets Registry
 */
package afc.rest;

import java.util.ArrayList;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;







public class Cache<K, T> {

	private long timeToLive;
	private LRUMap<K, CacheObject> cacheMap;

	@SuppressWarnings("rawtypes")
	private static Cache resCache;
	@SuppressWarnings("rawtypes")
	private static Cache infoCache;

	protected class CacheObject {
		public long lastAccessed = System.currentTimeMillis();
		public T value;

		protected CacheObject(T value) {
			this.value = value;
		}
	}  


	@SuppressWarnings("rawtypes")
	public static synchronized Cache getResCache(long timeToLive, long timerInterval, int maxItems) {
		if (resCache==null) {
			resCache=new Cache(timeToLive,timerInterval,maxItems);
		}
		return resCache;
	}


	@SuppressWarnings("rawtypes")
	public static synchronized Cache getInfoCache(long timeToLive, long timerInterval, int maxItems) {
		if (infoCache==null) {
			infoCache=new Cache(timeToLive,timerInterval,maxItems);
		}
		return infoCache;
	}

	private Cache(long timeToLive, final long timerInterval, int maxItems) {
		this.timeToLive = timeToLive * 1000;

		cacheMap = new LRUMap<K, CacheObject>(maxItems);

		if (timeToLive > 0 && timerInterval > 0) {

			Thread t = new Thread(new Runnable() {
				public void run() {
					while (true) {
						try {
							Thread.sleep(timerInterval * 1000);
						} catch (InterruptedException ex) {
						}
						cleanup();
					}
				}
			});

			t.setDaemon(true);
			t.start();
		}
	}

	public void put(K key, T value) {
		synchronized (cacheMap) {
			cacheMap.put(key, new CacheObject(value));
		}
	}

	//    @SuppressWarnings("unchecked")
	public T get(K key) {
		synchronized (cacheMap) {
			CacheObject c = cacheMap.get(key);
			if (c == null)
				return null;
			else {
				c.lastAccessed = System.currentTimeMillis();
				return c.value;
			}
		}
	}

	public void remove(K key) {
		synchronized (cacheMap) {
			cacheMap.remove(key);
		}
	}

	public int size() {
		synchronized (cacheMap) {
			return cacheMap.size();
		}
	}

	//    @SuppressWarnings("unchecked")
	public void cleanup() {

		long now = System.currentTimeMillis();
		ArrayList<K> deleteKey = null;

		synchronized (cacheMap) {
			MapIterator<K, CacheObject> itr = cacheMap.mapIterator();

			deleteKey = new ArrayList<K>((cacheMap.size() / 2) + 1);
			K key = null;
			CacheObject c = null;

			while (itr.hasNext()) {
				key = itr.next();
				c = itr.getValue();

				if (c != null && (now > (timeToLive + c.lastAccessed))) {
					deleteKey.add(key);
				}
			}
		}

		for (K key : deleteKey) {
			synchronized (cacheMap) {
				cacheMap.remove(key);
			}

			Thread.yield();
		}
	}
}