package com.covilla.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.covilla.repository.mongodb.BaseMongoDao;
import com.covilla.model.mongo.BaseModel;
import com.covilla.util.ValidatorUtil;
import jodd.typeconverter.Convert;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class BaseMongoService<D extends BaseModel> {

	public MongoOperations getMongoTemplate() {
		return getBaseMongoDao().getMongoTemplate();
	}

	protected abstract BaseMongoDao<D> getBaseMongoDao();

	/**
	 * 查找全表
	 * @return
	 */
	public List<D> findAll() {
		return getBaseMongoDao().findAll();

	}
	public List<D> findAll(String colName){
		return getBaseMongoDao().findAll(colName);
	}


	/**
	 * 更新不为空的项
	 * @param d
     */
	public void updateDocument(D d) {
		getBaseMongoDao().updateDocument(d);
	}


	/**
	 * 根据表达式查找
	 * @param regex
	 * @return
     */
	public List<D> findByRegex(String regex) {
		return getBaseMongoDao().findByRegex(regex);
	}

	/**
	 * 根据_id查找
	 * @param _id
	 * @return
     */
	public D findBy_id(Object _id) {
		return getBaseMongoDao().findBy_id(_id);
	}

	public D findBy_id(Object _id, String colName) {
		return getBaseMongoDao().findBy_id(_id, colName);
	}

	/**
	 * 根据id查找
	 * @param id
	 * @return
     */
	public D findById(Object id){
		return getBaseMongoDao().findById(id);
	}


	/**
	 * 插入document
	 * @param d
     */
	public void insert(D d) {
		getBaseMongoDao().insert(d);
	}


	/**
	 * 删除collection
	 */
	public void removeAll() {
		getBaseMongoDao().removeAll();
	}
	/**
	 * 根据Id删除
	 * @param _id
     */
	public void removeBy_id(ObjectId _id) {
		getBaseMongoDao().removeBy_id(_id);
	}

	/**
	 * 按条件查找
	 * @param columeName
	 * @param value
     * @return
     */
	public List<D> findByProperty(String columeName, Object value){
		return getBaseMongoDao().findByProperty(columeName, value);
	}

	/**
	 * 根据map查询
	 * @param map
	 * @return
     */
	public List<D> findByMap(Map<String, Object> map){
		return getBaseMongoDao().findByMapEntries(map);
	}

	public Long countByMap(Map<String, Object> map){
		return getBaseMongoDao().countByMap(map);
	}

	public void updateByMap(Map<String, Object> queryMap, Map<String, Object> updateMap){
		getBaseMongoDao().updateByMap(queryMap, updateMap);
	}

	/**
	 * 逻辑删除数据
	 * @param collection
	 * @param fieldName
     */
	public void deleteByValues(Collection collection, String fieldName){
		getBaseMongoDao().deleteByValues(collection, fieldName);
	}

	/**
	 * 生成documentId
	 * @param colName
	 * @return
     */
	public Integer generateDocumentId(String colName){
		List<D> list = getBaseMongoDao().findAllIds(colName);
		Integer maxId = 0;
		for (D d : list){
			JSONObject object = JSON.parseObject(JSONObject.toJSONString(d));
			if(ValidatorUtil.isNotNull(object.get("id")) && Convert.toInteger(object.get("id"))>maxId){
				maxId = Convert.toInteger(object.get("id"));
			}
		}
		return maxId + 1;
	}

	/**
	 * 生成第二级id，如category在shop中的id
	 * @param firstLevelId
	 * @param firstFieldName
     * @return
     */
	public Integer generate2thLevelId(ObjectId firstLevelId, String firstFieldName){
		D d = findBy_id(firstLevelId);
		JSONObject object = JSON.parseObject(JSONObject.toJSONString(d));
		JSONArray array = object.getJSONArray(firstFieldName);
		return findUntakenId(array, null);
	}

	/**
	 * 生成第三级id，如children在category的id
	 * @param firstLevelId
	 * @param firstFieldName
	 * @param secondLevelId
	 * @param secondFieldName
     * @return
     */
	public Integer generate3thLevelId(ObjectId firstLevelId, String firstFieldName, String secondLevelId, String secondFieldName, boolean thirdIdContain2thId){
		D d = findBy_id(firstLevelId);
		JSONObject object = JSON.parseObject(JSONObject.toJSONString(d));
		JSONArray array = object.getJSONArray(firstFieldName);
		for (int i=0; i<array.size(); i++){
			JSONObject object1 = array.getJSONObject(i);
			if(secondLevelId.equals(object1.getString("id"))){
				JSONArray array1 = object1.getJSONArray(secondFieldName);
				return findUntakenId(array1, thirdIdContain2thId?secondLevelId:null);
			}
		}
		return 1;
	}

	/**
	 * 从array中找到未占用的id
	 * @param array
	 * @return
     */
	public Integer findUntakenId(JSONArray array, String idPrefix){
		if(ValidatorUtil.isNull(array)){
			return 1;
		}
		Integer untakenId = 1;
		if (ValidatorUtil.isNotNull(array)){
			while (true){
				boolean exist = false;
				for (int i=0; i<array.size(); i++){
					JSONObject object1 = array.getJSONObject(i);
					String idStr1 = object1.getString("id");
					if(ValidatorUtil.isNotNull(idPrefix)&&idStr1.indexOf(idPrefix)==0){
						idStr1 = idStr1.replaceFirst(idPrefix, "");
					}
					if(untakenId.equals(Convert.toInteger(idStr1))){
						exist = true;
					}
				}
				if(exist){
					untakenId ++;
				}else {
					break;
				}
			}
		}
		return untakenId;
	}

	/**
	 * 修改最後更改時間
	 * @param collectionName
	 * @param _id
	 * @param filedName
     */
	public void updateModifyTime(String collectionName, ObjectId _id, String filedName){
		getBaseMongoDao().updateModifyTime(collectionName, _id, filedName);
	}

}
