package tfip.paf.day27.Repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import tfip.paf.day27.Models.Comment;
import tfip.paf.day27.Models.Show;

@Repository
public class ShowRepository {
    
    public static final String C_TVSHOWS = "tvshows";

	@Autowired
	private MongoTemplate mongoTemplate;

    public List<Show> getShow(String name) {
		Criteria criteria = Criteria.where("name").regex(name,"i");

		Query query = Query.query(criteria).with(Sort.by(Sort.Direction.ASC, "name"));;

		query.fields()
			.include("name", "id")
			.exclude("_id");

        System.out.println(mongoTemplate.find(query, Document.class, C_TVSHOWS)
        .stream().map(doc -> doc.toJson()).map(Show::toShow).toList());

        return mongoTemplate.find(query, Document.class, C_TVSHOWS)
            .stream()
            .map(doc -> doc.toJson())
            .map(Show::toShow)
            .toList();
    }

    /*
     * db.tvshows.update(
     *  {id:123},
     *  {$push:}
     * )
     */
    public void insertComment(Comment comment) {
        Document doc = new Document();
        doc.put("username",comment.getUsername());
        doc.put("rating",comment.getRating());
        doc.put("comment",comment.getComment());
        
        Criteria criteria = Criteria.where("id").is(comment.getId());
        Query query = Query.query(criteria);

        Update updateOps = new Update().push("comments",doc);
        UpdateResult result = mongoTemplate.upsert(query,updateOps,Document.class,C_TVSHOWS);
        System.out.printf("matched: %d\n",result.getMatchedCount());
        System.out.printf("modified: %d\n",result.getModifiedCount());
    }

    /*
     * db.tvshows.aggregate([
     *  {$match: { type: 'a type'}
     * }])
     */
    public void findShowsByType(String type) {
        // stages
        Criteria criteria = Criteria.where("type").regex(type,"i");
        MatchOperation matchType = Aggregation.match(criteria);

        ProjectionOperation selectFields = Aggregation.project("id","name").andExclude("_id");

        // pipeline
        Aggregation pipeline = Aggregation.newAggregation(matchType,selectFields);

        // perform query
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline,C_TVSHOWS,Document.class);

        printIt(results);
    }

    public void groupShowsByTimezone() {
        // Stage
        GroupOperation tzGroup = Aggregation.group("network.country.timezone")
            .count().as("total_shows")
            .push("name").as("titles");

        // pipeline
        Aggregation pipeline = Aggregation.newAggregation(tzGroup);

        // perform query
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline,C_TVSHOWS,Document.class);

        printIt(results);
    }

    private void printIt(AggregationResults<Document> results) {
        for (Document d : results) {
            System.out.printf(">>> %s\n", d.toJson());
        }
    }

    public void summarizeShows(String type) {
        // stage
        MatchOperation filterByType = Aggregation.match(
            Criteria.where("type").regex(type,"i"));
        ProjectionOperation summarizeFields = Aggregation.project("id","type")
            .and(
                AggregationExpression.from(
                    MongoExpression.create("""
                        $concat: [ \"$name\", \" (\", {$toString:\"$runtime\"}, \"mins)\"]
                    """
                    )
                )
            ).as("title")
            .andExclude("_id");
        SortOperation orderByTitle = Aggregation.sort(
            Sort.by(Direction.ASC,"title"));

        // pipeline
        Aggregation pipeline = Aggregation.newAggregation(filterByType,summarizeFields,orderByTitle);

        // perform query
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline,C_TVSHOWS,Document.class);

        printIt(results);
    }

    public void showCategories() {
        // stages
        UnwindOperation unwindGenres = Aggregation.unwind("genres");
        GroupOperation groupGenres = Aggregation.group("abc")
            .addToSet("genres").as("categories");

        // pipeline
        Aggregation pipeline = Aggregation.newAggregation(unwindGenres,groupGenres);

        // perform query
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline,C_TVSHOWS,Document.class);

        printIt(results);
    }

}
