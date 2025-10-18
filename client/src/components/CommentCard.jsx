import { useState, useEffect } from "react"; 
import "../styles/CommentCard.css";

import { CommentAPI } from "../services/APIService.js";

function CommentCard({ orgId, c, isAuthenticated, onRequireLogin, setBlameInfo, setBlameModalVisible }) {

    const [likeToggle, setLikeToggle] = useState(c.vote == 'like');
    const [dislikeToggle, setDislikeToggle] = useState(c.vote === 'dislike');

    const openBlameModal = (user, text, comment_id) => {
        if (!isAuthenticated) {
            onRequireLogin();
            return;
        }
        setBlameInfo({comment_id: comment_id, user: user, text: text}); // send info to CharityPage
        setBlameModalVisible(true);     // open the BlameModal
    };

    const toggleLikeDislike = (what) => {

        const setPos = what ? setLikeToggle : setDislikeToggle;
        const setNeg = what ? setDislikeToggle : setLikeToggle;
        const pos = what ? likeToggle : dislikeToggle;

        if (pos) {
            (async () => {
                try {
                    const r = await CommentAPI.deleteCommentVote({
                        comment_id: c.comment_id,
                        charity: orgId
                    });

                    if (r.status !== 200) {
                        console.log(r);
                        return;
                    }
                    
                    setPos(false);

                } catch (error) {
                    console.log(error)
                    return;
                }
            })();
        } else {
            (async () => {
                try {
                    const r = await CommentAPI.voteOnComment({
                        comment_id: c.comment_id,
                        charity: orgId,
                        vote: what
                    });

                    if (r.status !== 200) {
                        console.log(r)
                        return;
                    }

                    setNeg(false);
                    setPos(true);
                } catch (error) {
                    console.log(error);
                    return;
                }
            })();
        }
    };

    return <div className="comment-card">
      <div className="comment-header">
        <div className="comment-meta">
          <strong>{c.user}</strong>
          <span className="comment-date">{c.date ? new Date(c.date).toLocaleString() : ''}</span>
        </div>
        <button className={likeToggle ? "selected-like-dislike-btn" : "like-dislike-report-btn"}
          onClick={() => toggleLikeDislike(true)}>
          <img src={'http://localhost:8080/api/files/public/thumbs-up.png'} alt="like"/>
        </button>
        <button className={dislikeToggle ? "selected-like-dislike-btn" : "like-dislike-report-btn"}
          onClick={() => toggleLikeDislike(false)}>
          <img src={'http://localhost:8080/api/files/public/thumbs-down.png'} alt="dislike"/>
        </button>
        <button className="like-dislike-report-btn"
          onClick={() => openBlameModal(c.user, c.text, c.comment_id)}
          aria-label="Report"
          title="Report comment">
          <img src={'http://localhost:8080/api/files/public/blame-icon.png'} alt="Report comment"
            className="like-dislike-report-icon"/>
        </button>
      </div>
      <p>{c.text}</p>
    </div>
}

export default CommentCard
