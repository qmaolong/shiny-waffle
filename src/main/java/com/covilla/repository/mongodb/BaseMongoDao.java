package com.covilla.repository.mongodb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.covilla.model.mongo.BaseModel;
import com.covilla.util.ValidatorUtil;
import com.covilla.vo.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class BaseMongoDao<D extends BaseModel> {
	@Autowired
	private MongoOperations mongoTemplate;
	protected Class<D> clazz;

	@SuppressWarnings("unchecked")
	public BaseMongoDao() {
		@SuppressWarnings("rawtypes")
		Class clazz = getClass();
		while (clazz != Object.class) {
			Type t = clazz.getGenericSuperclass();
			if (t instanceof ParameterizedType) {
				Type[] args = ((ParameterizedType) t).getActualTypeArguments();
				if (args[0] instanceof Class) {
					this.clazz = (Class<D>) args[0];
					break;
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	public MongoOperations getMongoTemplate() {
		return mongoTemplate;
	}

	/**
	 * 查找全表
	 * @return
	 */
	public List<D> findAll() {
		return getMongoTemplate().find(new Query(), clazz);

	}
	public List<D> findAll(String colName){
		return getMongoTemplate().find(new Query(), clazz, colName);
	}

	/**
	 * 根据_id查找
	 * @param _id
	 * @return
	 */
	public D findBy_id(Object _id) {
		return getMongoTemplate().findOne(new Query(Criteria.where("_id").is(_id)), clazz);
	}

	public D findBy_id(Object _id, String colName) {
		return getMongoTemplate().findOne(new Query(Criteria.where("_id").is(_id)), clazz, colName);
	}

	/**
	 * 根据id查找
	 * @param id
	 * @return
	 */
	public D findById(Object id){
		return getMongoTemplate().findOne(new Query(Criteria.where("id").is(id)), clazz);
	}

	/**
	 * 更新整个document
	 * @param d
	 */
	public void updateDocument(D d) {
		if (ValidatorUtil.isNull(d.get_id())){
			return;
		}
		Query query = new Query(Criteria.where("_id").is(d.get_id()));
		Update update = new Update();

		DBObject dbObject = new BasicDBObject();
		mongoTemplate.getConverter().write(d, dbObject);

		for (String key : dbObject.keySet()){
			update.set(key, dbObject.get(key));
		}

		getMongoTemplate().updateFirst(query, update, clazz);
	}

	/**
	 * 根据map值查询
	 * @param map
	 * @return
     */
	public List<D> findByMapEntries(Map<String, Object> map){
		Criteria criteria = new Criteria();
		for(Map.Entry<String, Object> entry:map.entrySet()){
			criteria.and(entry.getKey()).is(entry.getValue());
		}
		return getMongoTemplate().find(new Query(criteria), clazz);
	}

	public Long countByMap(Map<String, Object> map){
		Criteria criteria = new Criteria();
		for(Map.Entry<String, Object> entry:map.entrySet()){
			criteria.and(entry.getKey()).is(entry.getValue());
		}
		return getMongoTemplate().count(new Query(criteria), clazz);
	}

	public void updateByMap(Map<String, Object> queryMap, Map<String, Object> updateMap){
		Criteria criteria = new Criteria();
		for(Map.Entry<String, Object> entry:queryMap.entrySet()){
			criteria.and(entry.getKey()).is(entry.getValue());
		}
		Update update = new Update();
		for(Map.Entry<String, Object> entry:updateMap.entrySet()){
			update.set(entry.getKey(), entry.getValue());
		}
		getMongoTemplate().updateFirst(new Query(criteria), update, clazz);
	}

	/**
	 * 以实体类非空值为条件查询
	 * @param example
	 * @return
     */
	public List<D> findByExample(D example){
		JSONObject object = JSON.parseObject(JSONObject.toJSONString(example));

		Criteria criteria = new Criteria();
		for (String key : object.keySet()){
			if(ValidatorUtil.isNotNull(object.get(key))){
				criteria.and(key).is(key);
			}
		}
		return getMongoTemplate().find(new Query(criteria), clazz);
	}


	/**
	 * 根据表达式查找
	 * @param regex
	 * @return
     */
	public List<D> findByRegex(String regex) {
		Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
		Criteria criteria = new Criteria("name").regex(pattern.toString());
		return getMongoTemplate().find(new Query(criteria), clazz);

	}

	public List<D> findAllIds(String fieldName){
		DBObject dbObject = new BasicDBObject();
//		dbObject.put("id", "");
		DBObject fieldObject = new BasicDBObject();
		fieldObject.put("id", true);
		BasicQuery basicQuery = new BasicQuery(dbObject, fieldObject);
		return getMongoTemplate().find(basicQuery, clazz, fieldName);
	}

	/**
	 * 根据Id查找
	 * @param id
	 * @return
     */
	public D findOne(Object id) {
		return getMongoTemplate().findOne(new Query(Criteria.where("_id").is(id)), clazz);
	}

	public D findOne(Object id, String colName) {
		return getMongoTemplate().findOne(new Query(Criteria.where("_id").is(id)), clazz, colName);
	}


	/**
	 * 插入document
	 * @param D
     */
	public void insert(D D) {
		getMongoTemplate().insert(D);
	}


	/**
	 * 删除collection
	 */
	public void removeAll() {
		List<D> list = this.findAll();
		if(list != null){
			for(D D : list){
				getMongoTemplate().remove(D);
			}
		}

	}
	/**
	 * 根据Id删除
	 * @param _id
     */
	public void removeBy_id(ObjectId _id) {
		Criteria criteria = Criteria.where("_id").is(_id);
		Query query = new Query(criteria);
		getMongoTemplate().remove(query, clazz);
	}

	/**
	 * 按条件查找
	 * @param columeName
	 * @param value
     * @return
     */
	public List<D> findByProperty(String columeName, Object value){
		Criteria criteria = Criteria.where(columeName).is(value);
		return getMongoTemplate().find(new Query((criteria)), clazz);
	}

	/**
	 * 逻辑删除，更新状态为1
	 * @param collection
	 * @param fieldName
     */
	public void deleteByValues(Collection collection, String fieldName){
		Query query = new Query(Criteria.where(fieldName).in(collection));
		Update update = new Update().set("dataStatus", 1);
		getMongoTemplate().updateMulti(query, update, clazz);
	}

	/**
	 * 分页查询
	 * @param criteria
	 * @param request
     * @return
     */
	public PageInfo<D> findPage(Criteria criteria, HttpServletRequest request){
		Integer size = Convert.toInteger(request.getParameter("rows"));
		Integer page = Convert.toInteger(request.getParameter("page"));

		Query query = new Query(criteria);
		if(ValidatorUtil.isNotNull(size)&&ValidatorUtil.isNotNull(page)){
			Long itemCount = getMongoTemplate().count(query, clazz);
			Long pageSize = itemCount%size==0?itemCount/size:itemCount/size+1;
			query = query.limit(size).skip((page-1)*size);
		}

		List<D> list = getMongoTemplate().find(query, clazz);
		PageInfo<D> result = new PageInfo<D>(list);
		result.setPageNum(page);

		return result;
	}

	/**
	 * 修改最後更改時間
	 * @param collectionName
	 * @param _id
	 * @param filedName
	 */
	public void updateModifyTime(String collectionName, ObjectId _id, String filedName){
		Query query = new Query(Criteria.where("_id").is(_id));
		Update update = new Update().set("lastModified." + filedName, new Date());
		getMongoTemplate().updateFirst(query, update, clazz, collectionName);
	}
}
